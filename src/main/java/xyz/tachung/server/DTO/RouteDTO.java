package xyz.tachung.server.DTO;

import lombok.Data;

@Data
public class RouteDTO {
    private Long userId; // User의 ID
    private String routename;
    private Long p1;
    private Long p2;
    private Long p3;
    private Long p4;
    private Long p5;
    private Long p6;

    // 기본 생성자, 모든 필드를 위한 getters and setters
}