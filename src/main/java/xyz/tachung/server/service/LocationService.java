package xyz.tachung.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class LocationService {

    @Value("${google.places.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;


    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String findPlacesNearby(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + latitude + "," + longitude +
                "&radius=10000" + // 1000 미터 반경
                "&type=tourist_attraction" + // 또는 'tourist_attraction'
                "&language=ko" +
                "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }

    // 이름으로 검색하여 json 파일 가져옴
    public String findPlacesByName(String query) {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json" +
                "?query=" + query +
                "&language=ko" +
                "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}
