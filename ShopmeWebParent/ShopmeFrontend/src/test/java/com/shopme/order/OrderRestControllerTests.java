package com.shopme.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithUserDetails("placidmasvidal@gmail.com")
  public void testSendOrderReturnRequestFailed() throws Exception {
    Integer orderId = 9999;
    OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, "", "");

    String requestURL = "/orders/return";

    mockMvc
        .perform(
            post(requestURL)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(returnRequest)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @WithUserDetails("placidmasvidal@gmail.com")
  public void testSendOrderReturnRequestSuccessful() throws Exception {
    Integer orderId = 4;
    String reason = "I bought the wrong items";
    String note = "Please return my money";

    OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, reason, note);

    String requestURL = "/orders/return";

    mockMvc
        .perform(
            post(requestURL)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(returnRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
