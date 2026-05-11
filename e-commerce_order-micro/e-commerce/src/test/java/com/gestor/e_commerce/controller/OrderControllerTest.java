package com.gestor.e_commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestor.e_commerce.dto.OrderDTO;
import com.gestor.e_commerce.dto.request.OrderRequestDTO;
import com.gestor.e_commerce.exception.OrderNotFoundException;
import com.gestor.e_commerce.service.JwtService;
import com.gestor.e_commerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // disables JWT security (ROLES)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;


    // CREATE ORDER - OK
    @Test
    void shouldCreateOrder_ok() throws Exception {

        OrderRequestDTO request = new OrderRequestDTO();
        request.setProductName("Laptop");
        request.setPrice(1000.0);
        request.setQuantity(1);
        request.setUserId(1L);

        OrderDTO response = OrderDTO.builder()
                .id(1L)
                .productName("Laptop")
                .price(1000.0)
                .quantity(1)
                .userId(1L)
                .username("Ivan")
                .build();

        when(service.createOrder(any())).thenReturn(response);

        mockMvc.perform(post("/e-commerce/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }


    // CREATE ORDER - KO
    @Test
    void shouldFailCreateOrder_whenInvalid() throws Exception {

        OrderRequestDTO request = new OrderRequestDTO(); // vacío

        mockMvc.perform(post("/e-commerce/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    // GET ALL - OK
    @Test
    void shouldGetAllOrders_ok() throws Exception {

        List<OrderDTO> list = List.of(
                OrderDTO.builder().id(1L).productName("Laptop").build()
        );

        when(service.getAllOrders()).thenReturn(list);

        mockMvc.perform(get("/e-commerce/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"));
    }


    // GET BY ID - OK
    @Test
    void shouldGetOrderById_ok() throws Exception {

        OrderDTO dto = OrderDTO.builder()
                .id(1L)
                .productName("Laptop")
                .build();

        when(service.getOrderById(99L))
                .thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(get("/e-commerce/order/99"))
                .andExpect(status().isNotFound());
    }


    // GET BY ID - KO
    @Test
    void shouldFailGetOrderById_whenNotFound() throws Exception {

        when(service.getOrderById(99L))
                .thenThrow(new RuntimeException("ERROR"));

        mockMvc.perform(get("/e-commerce/order/99"))
                .andExpect(status().isInternalServerError()); // ⚠️ luego será 404 con exception handler
    }


    // TOTAL - OK
    @Test
    void shouldReturnTotal_ok() throws Exception {

        when(service.getTotalOrdersPrice()).thenReturn(300.0);

        mockMvc.perform(get("/e-commerce/order/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("300.0"));
    }


    // EXPENSIVE - OK
    @Test
    void shouldGetExpensiveOrders_ok() throws Exception {

        List<OrderDTO> list = List.of(
                OrderDTO.builder().id(1L).price(200.0).build()
        );

        when(service.getOrdersPriceMoreThan(100.0)).thenReturn(list);

        mockMvc.perform(get("/e-commerce/order/expensive")
                        .param("price", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(200));
    }


    // EXPENSIVE - KO
    @Test
    void shouldFailExpensive_withoutParam() throws Exception {

        mockMvc.perform(get("/e-commerce/order/expensive"))
                .andExpect(status().isBadRequest());
    }


    // DELETE - OK
    @Test
    void shouldDeleteOrder_ok() throws Exception {

        doNothing().when(service).deleteOrder(1L);

        mockMvc.perform(delete("/e-commerce/order/1"))
                .andExpect(status().isNoContent());
    }


    // DELETE - KO
    @Test
    void shouldFailDelete_whenNotFound() throws Exception {

        doThrow(new OrderNotFoundException(99L))
                .when(service).deleteOrder(99L);

        mockMvc.perform(delete("/e-commerce/order/99"))
                .andExpect(status().isNotFound());
    }
}
