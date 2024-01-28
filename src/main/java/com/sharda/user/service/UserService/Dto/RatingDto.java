package com.sharda.user.service.UserService.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharda.user.service.UserService.model.Hotel;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingDto {
    private String ratingId;
    private String userId;
    private String hotelId;
    private int rating;
    private String feedback;
    private Date createdTime;
    private Hotel hotel;
}
