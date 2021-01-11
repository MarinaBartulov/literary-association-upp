package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long merchantId;
    private String currency;
    private double amount;
}
