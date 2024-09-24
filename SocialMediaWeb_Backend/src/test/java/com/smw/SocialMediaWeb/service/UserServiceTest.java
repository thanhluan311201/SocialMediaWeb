package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.UserCreationRequest;
import com.smw.SocialMediaWeb.dto.response.UserResponse;
import com.smw.SocialMediaWeb.entity.Role;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;
    private User user;


    @BeforeEach
    void initData(){
        dob = LocalDate.of(2001,12,30);

        request = UserCreationRequest.builder()
                .username("thanhluan")
                .firstname("dang")
                .lastname("luan")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("384637")
                .username("thanhluan")
                .firstname("dang")
                .lastname("luan")
                .dob(dob)
                .build();

        user = User.builder()
                .id("384637")
                .username("thanhluan")
                .firstname("dang")
                .lastname("luan")
                .dob(dob)
                .build();

    }

    @Test
    void createUser_validRequest_success(){
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(request);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("384637");
        Assertions.assertThat(response.getUsername()).isEqualTo("thanhluan");
        Assertions.assertThat(response.getFirstname()).isEqualTo("dang");
        Assertions.assertThat(response.getLastname()).isEqualTo("luan");
        Assertions.assertThat(response.getDob()).isEqualTo("2001-12-30");

    }

    @Test
    void createUser_userExisted_fail(){
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);

        //WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        //THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);

    }
}
