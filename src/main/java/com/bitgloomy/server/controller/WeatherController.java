package com.bitgloomy.server.controller;

import com.bitgloomy.server.domain.ConvertWeatherData;
import com.bitgloomy.server.domain.Weather;
import com.bitgloomy.server.dto.RequestWeatherDTO;
import com.bitgloomy.server.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/weather")
    public ResponseEntity<?> requestAPI(@RequestBody RequestWeatherDTO requestWeatherDTO){
        try {
            ConvertWeatherData convertWeatherData = weatherService.requestAPI(requestWeatherDTO);
            return ResponseEntity.status(HttpStatus.OK).body(convertWeatherData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
