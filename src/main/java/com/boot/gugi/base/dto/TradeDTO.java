package com.boot.gugi.base.dto;

import com.boot.gugi.base.Enum.Category;
import com.boot.gugi.base.Enum.TradeMethod;
import com.boot.gugi.base.Enum.TradeStatus;
import com.boot.gugi.model.TradeImage;
import com.boot.gugi.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Builder
@Getter
public class TradeDTO {
    @Data
    @Setter
    public static class TradeListDTO{
        private UUID tradeId;
        private String title;
        private Integer price;
        private TradeStatus tradeStatus;
        private List<TradeMethod> tradeMethod;
        private String location;
        private String thumbnail;
    }

    @Data
    @Builder
    public static class CreateTradeDTO{

        private Category category;

        private String title;

        private String content;

        private Integer price;

        private List<TradeMethod> methods;

        private String location;

        private String contact;
    }

    @Data
    @Getter
    public static class DetailTradeDTO{

        private Long userId;

        private List<String> images;

        private Category category;
        private String title;

        private String content;

        private Integer price;

        private TradeStatus status;

        private List<TradeMethod> methods;

        private String location;

        private String contact;
    }
}
