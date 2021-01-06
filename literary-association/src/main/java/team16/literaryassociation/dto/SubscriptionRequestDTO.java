package team16.literaryassociation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String currency;

    private String merchantEmail;

    private String merchantId;

}
