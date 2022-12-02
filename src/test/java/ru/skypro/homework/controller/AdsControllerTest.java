package ru.skypro.homework.controller;

import org.apache.catalina.security.SecurityConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
@WebMvcTest
class AdsControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private AdsController adsController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(adsController).isNotNull();
    }

    @Test
    void addAds() {
    }

    @Test
    void getAllAds() {
    }

    @Test
    void getAds() {
    }

    @Test
    void getAdsMe() {
    }

    @Test
    @WithAnonymousUser
    void updateAdsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .get("/ads/1")
                                .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void updateAdsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/1")
                        .with(csrf())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void updateImage() {
    }

    @Test
    void removeAds() {
    }

    @Test
    void addAdsComment() {
    }

    @Test
    void getAdsComments() {
    }

    @Test
    void getAdsComment() {
    }

    @Test
    void deleteAdsComment() {
    }

    @Test
    void updateAdsComment() {
    }
}