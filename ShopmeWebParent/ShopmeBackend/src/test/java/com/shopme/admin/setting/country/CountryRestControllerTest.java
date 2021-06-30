package com.shopme.admin.setting.country;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @Test
  @WithMockUser(
      username = "placidmasvidal@gmail.com",
      password = "someRandomPassword",
      roles = {"ADMIN"})
  public void testListCountries() throws Exception {
    String url = "/countries/list";
    MvcResult mvcResult =
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();

    String jsonResponse = mvcResult.getResponse().getContentAsString();
//    System.out.println("jsonResponse = " + jsonResponse);

    Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
//    Arrays.stream(countries).forEach(System.out::println);

    assertThat(countries).hasSizeGreaterThan(0);
  }
}
