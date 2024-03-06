package xyz.tachung.server.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.tachung.server.DTO.TourResponse;
import xyz.tachung.server.Entity.TourPlace;
import xyz.tachung.server.repository.TourRepository;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Value("${tour.api-key}")
    private String apiKey;

    private final TourRepository tourRepository;

    private final RestTemplate restTemplate;

    public TourService(TourRepository tourRepository, RestTemplate restTemplate) {
        this.tourRepository = tourRepository;
        this.restTemplate = restTemplate;
    }

    public List<TourPlace> findPlacesByName(String query) {
        String json = fetchTourData(query); // API로부터 데이터를 가져옴
        List<TourResponse.Item> items = parseJson(json); // 가져온 데이터를 파싱
        List<TourPlace> tours = new ArrayList<>();

        for (TourResponse.Item item : items) {
            Optional<TourPlace> existingTour = tourRepository.findByTitleAndAddress(item.getTitle(), item.getAddr1());
            if (existingTour.isPresent()) {
                tours.add(existingTour.get());
            } else {
                TourPlace newTour = new TourPlace();
                newTour.setTitle(item.getTitle());
                newTour.setAddress(item.getAddr1());
                newTour.setImage(item.getFirstimage());
                newTour.setMapx(item.getMapx());
                newTour.setMapy(item.getMapy());
                tourRepository.save(newTour);
                tours.add(newTour);
            }
        }
        return tours;
    }

    public List<TourPlace> findPlacesByPositon(Double latitude, Double longitude) {
        String json = fetchTourPositionData(latitude, longitude); // API로부터 데이터를 가져옴
        List<TourResponse.Item> items = parseJson(json); // 가져온 데이터를 파싱
        List<TourPlace> tours = new ArrayList<>();

        for (TourResponse.Item item : items) {
            Optional<TourPlace> existingTour = tourRepository.findByTitleAndAddress(item.getTitle(), item.getAddr1());
            if (existingTour.isPresent()) {
                tours.add(existingTour.get());
            } else {
                TourPlace newTour = new TourPlace();
                newTour.setTitle(item.getTitle());
                newTour.setAddress(item.getAddr1());
                newTour.setImage(item.getFirstimage());
                newTour.setMapx(item.getMapx());
                newTour.setMapy(item.getMapy());
                tourRepository.save(newTour);
                tours.add(newTour);
            }
        }
        return tours;
    }

    public List<TourResponse.Item> parseJson(String json) {
        Gson gson = new Gson();
        TourResponse tourResponse = gson.fromJson(json, TourResponse.class);
        if (tourResponse != null && tourResponse.getResponse() != null &&
                tourResponse.getResponse().getBody() != null &&
                tourResponse.getResponse().getBody().getItems() != null) {
            return tourResponse.getResponse().getBody().getItems().getItem();
        } else {
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }


    public String fetchTourData(String query) {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + apiKey + // 이미 인코딩된 키
                    "&numOfRows=200" +
                    "&pageNo=1" +
                    "&MobileOS=ETC" +
                    "&MobileApp=AppTest" +
                    "&_type=json" +
                    "&listYN=Y" +
                    "&arrange=A" +
                    "&keyword=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()) +
                    "&contentTypeId=12";

            URI uri = new URI(url);
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String fetchTourPositionData(Double latitude, Double longitude) {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=" + apiKey + // 이미 인코딩된 키
                    "&numOfRows=200" +
                    "&pageNo=1" +
                    "&MobileOS=ETC" +
                    "&MobileApp=AppTest" +
                    "&_type=json" +
                    "&listYN=Y" +
                    "&arrange=A" +
                    "&mapX=" + longitude +
                    "&mapY=" + latitude +
                    "&radius=10000" +
                    "&contentTypeId=15";

            URI uri = new URI(url);
            return restTemplate.getForObject(uri, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<TourPlace> getTourPlaceById(Long id) {
        return tourRepository.findById(id);
    }

}
