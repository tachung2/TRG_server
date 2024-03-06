package xyz.tachung.server.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TourResponse {
    private Response response;

    @Data
    public class Response {
        private Body body;

    }
    @Data
    public class Body {
        private Items items;
    }
    @Data
    public class Items {
        private List<Item> item;

    }
    @Data
    public class Item {
        private String title;
        private String addr1;
        private String firstimage;
        private Double mapx;
        private Double mapy;
    }
}
