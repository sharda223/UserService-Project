package com.sharda.user.service.UserService.feignService;

import com.sharda.user.service.UserService.Dto.RatingDto;
import com.sharda.user.service.UserService.Dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    @GetMapping("/ratings/users/{userId}")
    List<RatingDto> getRating(@PathVariable("userId") String userId);
}
