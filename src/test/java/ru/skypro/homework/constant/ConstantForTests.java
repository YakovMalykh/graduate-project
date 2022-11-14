package ru.skypro.homework.constant;


import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.skypro.homework.dto.Role.USER;

public class ConstantForTests {
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE = "10-12-2000 10:11:11";
    public static final LocalDateTime PARSE_DATE = LocalDateTime.parse(DATE, DateTimeFormatter.ofPattern(DATE_FORMAT));
    public static final Ads ADS = new Ads();
    public static final User AUTHOR_1 = new User();
    public static final User AUTHOR_2 = new User();

    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String PHONE = "+79999999999";
    public static final String EMAIL = "user@mail.com";

    public static final String PASSWORD = "password";
    public static final Role ROLE = USER;

    public static final Ads TEST_ADS_1 = new Ads();
    public static final Ads TEST_ADS_2 = new Ads();
    public static final Comment TEST_COMMENT_1 = new Comment();
    public static final Comment TEST_COMMENT_2 = new Comment();
    public static final String TEXT = "text";
    public static final String TEXT_2 = "text 2";

    public static final String TITLE= "title";
    public static final String TITLE_2= "title 2";
    public static final Integer PRICE= Integer.valueOf("100");
    public static final Integer PRICE_2= Integer.valueOf("200");
    public static final String DESCRIPTION= "description";
    public static final String DESCRIPTION_2= "description 2";

    public static final String IMAGE_STR="image_str";
    public static final String IMAGE_2_STR="image2_str";

    public static final Image IMAGE=new Image();
    public static final List<Image> LIST_IMAGES= new ArrayList<>();
    public static final List<Ads> LIST_ADS= new ArrayList<>();
    public static final List<AdsDto>LIST_ADS_DTO=new ArrayList<>();

    public static final List<Comment> LIST_COMMENTS = new ArrayList<>();
    public static final List<Comment> EMPTY_LIST_COMMENTS = new ArrayList<>();
    public static final AdsCommentDto ADS_COMMENT_DTO = new AdsCommentDto();
    public static final AdsDto ADS_DTO = new AdsDto();
    public static final CreateAdsDto CREATE_ADS_DTO = new CreateAdsDto();
    public static final List<AdsCommentDto> LIST_ADS_COMMENT_DTO = new ArrayList<>();


    public static final User USER_1 = new User();
    public static final User USER_2 = new User();
    public static final UserDto USER_DTO_1 = new UserDto();
    public static final UserDto USER_DTO_2 = new UserDto();
    public static final RegisterReqDto REGISTER_REQ_DTO = new RegisterReqDto();
    public static final List<User> LIST_USER = new ArrayList<>();
    public static final NewPasswordDto NEW_PASSWORD_DTO = new NewPasswordDto();

    public static final List<UserDto> LIST_USER_DTO = new ArrayList<>();

}
