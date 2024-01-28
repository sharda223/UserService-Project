package com.sharda.user.service.UserService.service.impl;

import com.netflix.discovery.util.RateLimiter;
import com.sharda.user.service.UserService.Dto.RatingDto;
import com.sharda.user.service.UserService.Dto.UserDto;
import com.sharda.user.service.UserService.feignService.HotelService;
import com.sharda.user.service.UserService.feignService.RatingService;
import com.sharda.user.service.UserService.model.Hotel;
import com.sharda.user.service.UserService.model.Rating;
import com.sharda.user.service.UserService.repo.UserRepository;
import com.sharda.user.service.UserService.exception.UserHandledException;
import com.sharda.user.service.UserService.model.User;
import com.sharda.user.service.UserService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String BAD_REQUEST = "Bad Request";

    @Autowired
    UserRepository userRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HotelService hotelService;
    @Autowired
    RatingService ratingService;


    @Override
    public User saveUser(UserDto userDto) throws UserHandledException {
        log.info("Creating User");
        if (!(Objects.nonNull(userDto))) {
          throw new UserHandledException(BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        String randomId = UUID.randomUUID().toString().split("-")[0];
        user.setId(randomId);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        log.info("saving user in database");
        user = userRepository.save(user);
        return user;
    }

    @Override
    //@Cacheable(value = "User", key = "#userId")
    public UserDto getUser(String userId) throws UserHandledException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandledException("User with given id is not found : " + userId));
        //RatingDto[] getRatingFromUsers = restTemplate.getForObject("http://localhost:8082/ratings/users/"+user.getId(), RatingDto[].class);
       // log.info("{} ", getRatingFromUsers);
        UserDto userDto = new UserDto();
        List<RatingDto> ratings = ratingService.getRating(user.getId());
        List<RatingDto> getHotelDetailsInRating = ratings.stream()
                .map(rating -> {
                    //ResponseEntity<Hotel> getHotelDetails = restTemplate.getForEntity("http://localhost:8081/hotels/"+rating.getHotelId(), Hotel.class);
                    Hotel hotel = hotelService.getHotel(rating.getHotelId());
                  //  log.info("response status code: {} ", hotel.getStatusCode());
                    rating.setHotel(hotel);
                    rating.setRatingId(null);
                    rating.setHotelId(null);
                    rating.setUserId(null);
                    return rating;
                }).collect(Collectors.toList());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAbout(user.getAbout());
        userDto.setRatings(getHotelDetailsInRating);
        log.info("{} ", getHotelDetailsInRating);
        return userDto;
    }

    @Override
    //@Cacheable
    public List<User> getAllUser() throws UserHandledException {
        return userRepository.findAll();
    }

    @Override
    //@CachePut(value = "user", key = "user.id")
    public User updateUser(User user) throws UserHandledException {
        log.info("updating the user");
        if (!userRepository.existsById(user.getId())) {
            log.error("user does not exists with this id");
            throw new UserHandledException("No user exists", HttpStatus.BAD_REQUEST);
        }
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());
        log.info("user is updated");
        return userRepository.save(existingUser);
    }

    @Override
    //@CacheEvict(value = "User", key = "#userId")
    public String deleteUser(String userId) throws UserHandledException {
        if (!userRepository.existsById(userId)) {
            log.error("user does not exists with this id");
            throw new UserHandledException("No user exists", HttpStatus.BAD_REQUEST);
        }
        log.info("user deleted successfully");
        userRepository.deleteById(userId);
        return "user is deleted from database with this id: " + userId;
    }

    @Override
    //@Cacheable(value = "user")
    public List<User> getUsersByName(String name) throws UserHandledException {
        return userRepository.findByName(name);
    }

}
