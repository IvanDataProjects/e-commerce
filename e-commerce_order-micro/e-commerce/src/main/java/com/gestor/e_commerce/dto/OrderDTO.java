package com.gestor.e_commerce.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
    private Long userId;
    private String username;
}
