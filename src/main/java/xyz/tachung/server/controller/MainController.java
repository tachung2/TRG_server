package xyz.tachung.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.tachung.server.DTO.AuthenticationResponse;
import xyz.tachung.server.DTO.RouteDTO;
import xyz.tachung.server.DTO.UserInfo;
import xyz.tachung.server.Entity.Route;
import xyz.tachung.server.Entity.TourPlace;
import xyz.tachung.server.Entity.User;
import xyz.tachung.server.repository.UserRepository;
import xyz.tachung.server.service.LocationService;
import xyz.tachung.server.service.RouteService;
import xyz.tachung.server.service.TourService;
import xyz.tachung.server.service.UserService;
import xyz.tachung.server.util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MainController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final LocationService locationService;

    private final JwtUtil jwtUtil;

    private final TourService tourService;

    private final RouteService routeService;

    public MainController(UserService userService, UserRepository userRepository, LocationService locationService, JwtUtil jwtUtil, TourService tourService, RouteService routeService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.jwtUtil = jwtUtil;
        this.tourService = tourService;
        this.routeService = routeService;
    }


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User newUser = userService.signup(user);
            // 회원가입이 성공하면, User 객체와 함께 200 OK를 반환
            return ResponseEntity.ok(newUser);
        } catch (IllegalStateException e) {
            // 이미 존재하는 회원의 경우, Conflict (409) 상태 코드 반환
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("이미 존재하는 회원입니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            // 이메일과 비밀번호가 일치하는 경우
            String accessToken = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
            UserInfo userInfo = new UserInfo(existingUser.get().getName(), existingUser.get().getEmail());
            return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken, userInfo));  // 200 상태 코드 반환
        }
        // 이메일이 없거나 비밀번호가 일치하지 않는 경우
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 401 상태 코드 반환
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userService.getUserByEmail(email);
                if (user != null) {
                    // 사용자 정보를 Map 혹은 DTO 객체를 사용하여 JSON 형식으로 반환
                    Map<String, String> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId().toString());
                    userInfo.put("userName", user.getName());

                    return ResponseEntity.ok(userInfo);
                }
            }
        }
        return ResponseEntity.badRequest().body("Invalid Token or User not found");
    }

    @GetMapping("/current")
    public ResponseEntity<List<TourPlace>> getCurrentLocationPlaces(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude) {
        List<TourPlace> tours = tourService.findPlacesByPositon(latitude, longitude);

        return ResponseEntity.ok(tours);
    }

    // 이름으로 검색
    @GetMapping("/find")
    public String getFindPlace(@RequestParam String query) {
        return locationService.findPlacesByName(query);
    }


    @GetMapping("/search")
    public ResponseEntity<List<TourPlace>> getPlacesByName(@RequestParam String query) {
        List<TourPlace> tours = tourService.findPlacesByName(query);
        return ResponseEntity.ok(tours);
    }

//   유저 루트 조회
    @GetMapping("/route")
    public ResponseEntity<List<Route>> getRoutesByUserId(@RequestParam Long userId) {
        List<Route> routes = routeService.getRoutesByUserId(userId);
        return ResponseEntity.ok(routes);
    }

//    유저 루트 만들기 및 수정
@PostMapping("/route-create")
public ResponseEntity<Route> createRoute(@RequestBody RouteDTO routeDTO) {
    Route createdOrUpdatedRoute = routeService.createOrUpdateRoute(routeDTO);
    return ResponseEntity.ok(createdOrUpdatedRoute);
}

//    여행지 id로 조회
    @GetMapping("/place/{id}")
    public ResponseEntity<TourPlace> getTourPlaceById(@PathVariable Long id) {
        return tourService.getTourPlaceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{routeId}")
    public ResponseEntity<?> deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.ok().build();
    }

}
