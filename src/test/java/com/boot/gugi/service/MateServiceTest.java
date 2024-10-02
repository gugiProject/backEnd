package com.boot.gugi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.base.dto.MateRequestDTO;
import com.boot.gugi.base.dto.MateResponseDTO;
import com.boot.gugi.exception.MatePostException;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.MatePostApplicant;
import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.MatePostApplicantRepository;
import com.boot.gugi.repository.MatePostRepository;
import com.boot.gugi.repository.MatePostStatusRepository;
import com.boot.gugi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Nested
@DisplayName("직관 메이트 테스트")
public class MateServiceTest {

    @Autowired
    private MatePostStatusRepository matePostStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatePostApplicantRepository applicantRepository;

    @Autowired
    private MatePostService matePostService;

    private User owner;
    private User applicant;
    private MatePost createdPost;

    @BeforeEach
    public void setUp() {
        // Given User
        owner = createUser("김구기", "test@example.com", Sex.FEMALE, 25, Team.SAMSUNG);
        applicant = createUser("이철수", "applicant@example.com", Sex.MALE, 29, Team.SAMSUNG);
    }

    private User createUser(String name, String email, Sex gender, int age, Team team) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setGender(gender);
        user.setAge(age);
        user.setTeam(team);
        return userRepository.save(user);
    }

    private MateDTO createMateDTO(String title, String content, String contact, int participants,
                                  int totalMembers, LocalDate gameDate, Stadium stadium,
                                  GenderPreference gender, AgeGroup ageGroup, Team team) {
        MateDTO dto = new MateDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setContact(contact);
        dto.setParticipants(participants);
        dto.setTotalMembers(totalMembers);
        dto.setGameDate(gameDate);
        dto.setStadium(stadium);
        dto.setGender(gender);
        dto.setAgeGroup(ageGroup);
        dto.setTeam(team);
        return dto;
    }

    private MatePost createMatePost(String title, String content, String contact, int participants,
                                    int totalMembers, LocalDate gameDate, Stadium stadium,
                                    GenderPreference gender, AgeGroup ageGroup, Team team, User owner) {
        MateDTO dto = createMateDTO(title, content, contact, participants, totalMembers, gameDate, stadium, gender, ageGroup, team);
        return matePostService.createMatePost(dto, owner);
    }

    @Test
    @DisplayName("직관메이트 글 생성")
    public void createMatePostTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        // Then
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getParticipants()).isEqualTo(1);
        assertThat(createdPost.getTotalMembers()).isEqualTo(4);
        assertThat(createdPost.getTitle()).isEqualTo("Match Title");
        assertThat(createdPost.getContent()).isEqualTo("Match Content");
        assertThat(createdPost.getContact()).isEqualTo("contact@example.com");

        // Verify
        MatePostStatus postStatus = matePostStatusRepository.findByUser(owner).stream().findFirst().orElse(null);
        assertThat(postStatus).isNotNull();
        assertThat(postStatus.getPostStatus()).isEqualTo(MateStatus.CREATOR);
        assertThat(postStatus.getMatePost().getId()).isEqualTo(createdPost.getId());
        assertThat(postStatus.getUser().getId()).isEqualTo(owner.getId());
    }

    @Test
    @DisplayName("직관메이트 글 수정")
    public void updateMatePostTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        UUID postId = createdPost.getId();
        MateDTO updateDto = new MateDTO();
        updateDto.setTitle("Updated Title");
        updateDto.setContent("Updated Content");
        updateDto.setGameDate(LocalDate.now().plusDays(2));
        updateDto.setStadium(Stadium.SUWON);
        updateDto.setTotalMembers(5);
        updateDto.setTeam(Team.KIA);

        // When
        MatePost updatedPost = matePostService.updateMatePost(postId, updateDto);

        // Then
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getParticipants()).isEqualTo(createdPost.getParticipants());
        assertThat(updatedPost.getTotalMembers()).isEqualTo(5);
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
        assertThat(updatedPost.getContact()).isEqualTo(createdPost.getContact());
        assertThat(updatedPost.getGameDate()).isEqualTo(LocalDate.now().plusDays(2));
        assertThat(updatedPost.getStadium()).isEqualTo(Stadium.SUWON);
        assertThat(updatedPost.getTeam()).isEqualTo(Team.KIA);
    }

    @Test
    @DisplayName("직관메이트 매칭 신청")
    public void applyForMatePostTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        MateRequestDTO requestDTO = new MateRequestDTO();
        requestDTO.setPostId(createdPost.getId());
        requestDTO.setUserId(applicant.getId());

        // When
        matePostService.applyForMatePost(requestDTO);

        // Then
        MatePostApplicant applicantEntry = applicantRepository.findByApplicantIdAndMatePostId(applicant.getId(), createdPost.getId())
                .orElse(null);
        assertThat(applicantEntry).isNotNull();
        assertThat(applicantEntry.getStatus()).isEqualTo(MateStatus.PENDING);
        assertThat(applicantEntry.getMatePost().getId()).isEqualTo(createdPost.getId());
        assertThat(applicantEntry.getApplicant().getId()).isEqualTo(applicant.getId());

        MatePostStatus postStatus = matePostStatusRepository.findByUser(applicant).stream().findFirst().orElse(null);
        assertThat(postStatus).isNotNull();
        assertThat(postStatus.getPostStatus()).isEqualTo(MateStatus.PENDING);
        assertThat(postStatus.getMatePost().getId()).isEqualTo(createdPost.getId());
        assertThat(postStatus.getUser().getId()).isEqualTo(applicant.getId());
    }

    @Test
    @DisplayName("직관메이트 매칭 신청 - 본인 글 신청 에러")
    public void applyForMatePost_OwnerCannotApplyTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        MateRequestDTO requestDTO = new MateRequestDTO();
        requestDTO.setPostId(createdPost.getId());
        requestDTO.setUserId(owner.getId());

        // When & Then
        assertThatThrownBy(() -> matePostService.applyForMatePost(requestDTO))
                .isInstanceOf(MatePostException.class)
                .hasMessage("작성자는 자신의 게시물에 신청할 수 없습니다.");
    }

    @Test
    @DisplayName("직관메이트 매칭 승인")
    public void approveApplicationTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        MateRequestDTO requestDTO = new MateRequestDTO();
        requestDTO.setPostId(createdPost.getId());
        requestDTO.setUserId(applicant.getId());
        matePostService.applyForMatePost(requestDTO);

        MateResponseDTO responseDTO = new MateResponseDTO();
        responseDTO.setPostId(createdPost.getId());
        responseDTO.setUserId(applicant.getId());

        // When
        matePostService.approveApplication(responseDTO);

        // Then
        MatePostApplicant applicantEntry = applicantRepository.findByApplicantIdAndMatePostId(applicant.getId(), createdPost.getId())
                .orElse(null);
        assertThat(applicantEntry).isNotNull();
        assertThat(applicantEntry.getStatus()).isEqualTo(MateStatus.APPROVED);

        MatePostStatus postStatus = matePostStatusRepository.findByUser(applicant).stream().findFirst().orElse(null);
        assertThat(postStatus).isNotNull();
        assertThat(postStatus.getPostStatus()).isEqualTo(MateStatus.APPROVED);
        assertThat(createdPost.getParticipants()).isEqualTo(2);
    }

    @Test
    @DisplayName("직관메이트 신청 승인 - 모집 완료 에러")
    public void approveApplication_PostFullTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        createdPost.setParticipants(4);
        MateRequestDTO requestDTO = new MateRequestDTO();
        requestDTO.setPostId(createdPost.getId());
        requestDTO.setUserId(applicant.getId());
        matePostService.applyForMatePost(requestDTO);

        MateResponseDTO responseDTO = new MateResponseDTO();
        responseDTO.setPostId(createdPost.getId());
        responseDTO.setUserId(applicant.getId());

        // When & Then
        assertThatThrownBy(() -> matePostService.approveApplication(responseDTO))
                .isInstanceOf(MatePostException.class)
                .hasMessage("모집이 이미 완료되었습니다. 더 이상 신청을 승인할 수 없습니다.");
    }

    @Test
    @DisplayName("직관메이트 신청 거절")
    public void rejectApplicationTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        MateRequestDTO requestDTO = new MateRequestDTO();
        requestDTO.setPostId(createdPost.getId());
        requestDTO.setUserId(applicant.getId());
        matePostService.applyForMatePost(requestDTO);

        MateResponseDTO responseDTO = new MateResponseDTO();
        responseDTO.setPostId(createdPost.getId());
        responseDTO.setUserId(applicant.getId());

        // When
        matePostService.rejectApplication(responseDTO);

        // Then
        MatePostApplicant applicantEntry = applicantRepository.findByApplicantIdAndMatePostId(applicant.getId(), createdPost.getId())
                .orElse(null);
        assertThat(applicantEntry).isNotNull();
        assertThat(applicantEntry.getStatus()).isEqualTo(MateStatus.REJECTED);

        MatePostStatus postStatus = matePostStatusRepository.findByUser(applicant).stream().findFirst().orElse(null);
        assertThat(postStatus).isNotNull();
        assertThat(postStatus.getPostStatus()).isEqualTo(MateStatus.REJECTED);
    }

}