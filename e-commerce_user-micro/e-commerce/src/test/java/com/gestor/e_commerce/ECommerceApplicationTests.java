package com.gestor.e_commerce;

import com.gestor.e_commerce.messaging.consumer.OrderConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ECommerceApplicationTests {

	@MockBean
	private OrderConsumer orderProducer;

	@Test
	void contextLoads() {
	}
}
