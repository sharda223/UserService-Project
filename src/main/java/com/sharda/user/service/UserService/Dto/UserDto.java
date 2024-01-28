package com.sharda.user.service.UserService.Dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String about;
    private List<RatingDto> ratings;
}
