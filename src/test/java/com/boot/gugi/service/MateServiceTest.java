package com.boot.gugi.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.MatePostRepository;
import com.boot.gugi.repository.MatePostStatusRepository;
import com.boot.gugi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MateServiceTest {

    @Autowired
    private MatePostRepository matePostRepository;

    @Autowired
    private MatePostStatusRepository matePostStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatePostService matePostService;

    private User owner;
    private MatePost createdPost;

    @BeforeEach
    public void setUp() {
        // Given User
        owner = createUser("김구기", "test@example.com", Sex.FEMALE, 25, Team.SAMSUNG);
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
    @DisplayName("직관메이트 post 생성")
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
    @DisplayName("직관메이트 post 수정")
    public void updateMatePostTest() {
        // Given
        createdPost = createMatePost("Match Title", "Match Content", "contact@example.com", 0, 4,
                LocalDate.now().plusDays(1), Stadium.DAEGU,
                GenderPreference.FEMALE_ONLY, AgeGroup.AGE_20s, Team.SAMSUNG, owner);

        Long postId = createdPost.getId();
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


}