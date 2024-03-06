package xyz.tachung.server.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TourPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String address;

    String image;

    Double mapx;

    Double mapy;
}
