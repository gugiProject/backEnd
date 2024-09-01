package com.boot.gugi.model;

import com.boot.gugi.base.Enum.Category;
import com.boot.gugi.base.Enum.TradeMethod;
import com.boot.gugi.base.Enum.TradeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="trade")
public class Trade {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID tradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Builder.Default
    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeImage> images = new ArrayList<>();

    @Column(nullable=false)
    private Category category;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private Integer price;

    @Column(nullable=false)
    private TradeStatus status;

    @Column(nullable=false)
    private List<TradeMethod> methods;

    @Column(nullable=false)
    private String location;

    @Column(nullable=false)
    private String contact;

}
