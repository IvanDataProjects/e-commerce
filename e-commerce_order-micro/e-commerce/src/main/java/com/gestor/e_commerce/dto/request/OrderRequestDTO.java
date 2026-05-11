package com.gestor.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull
    private Long userId;

    @NotBlank(message = "Product name must not be blank")
    private String productName;

    private Double price;
    private Integer quantity;
}
