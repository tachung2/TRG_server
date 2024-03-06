package xyz.tachung.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.tachung.server.DTO.RouteDTO;
import xyz.tachung.server.Entity.Route;
import xyz.tachung.server.Entity.User;
import xyz.tachung.server.repository.RouteRepository;
import xyz.tachung.server.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    public Route createOrUpdateRoute(RouteDTO routeDTO) {
        if (routeDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID는 null일 수 없습니다.");
        }

        // User ID로 User 엔티티 찾기
        User user = userRepository.findById(routeDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 User ID: " + routeDTO.getUserId()));

        Route route = new Route();
        // RouteDTO에서 Route 엔티티로 필드 복사
        route.setUser(user);
        route.setRoutename(routeDTO.getRoutename());
        route.setP1(routeDTO.getP1());
        route.setP2(routeDTO.getP2());
        route.setP3(routeDTO.getP3());
        route.setP4(routeDTO.getP4());
        route.setP5(routeDTO.getP5());
        route.setP6(routeDTO.getP6());

        // 기존 루트 업데이트 또는 새로운 루트 저장
        Optional<Route> existingRoute = routeRepository.findByUserIdAndRoutename(user.getId(), route.getRoutename());
        if (existingRoute.isPresent()) {
            updateRouteDetails(existingRoute.get(), route);
            return routeRepository.save(existingRoute.get());
        } else {
            return routeRepository.save(route);
        }
    }

    private void updateRouteDetails(Route existingRoute, Route newRoute) {
        existingRoute.setP1(newRoute.getP1()!= null ? newRoute.getP1() : null);
        existingRoute.setP3(newRoute.getP2() != null ? newRoute.getP2() : null);
        existingRoute.setP3(newRoute.getP3() != null ? newRoute.getP3() : null);
        existingRoute.setP4(newRoute.getP4() != null ? newRoute.getP4() : null);
        existingRoute.setP5(newRoute.getP5() != null ? newRoute.getP5() : null);
        existingRoute.setP6(newRoute.getP6() != null ? newRoute.getP6() : null);
        // ... 나머지 필드에 대한 업데이트도 동일한 방식으로 처리
    }

    public List<Route> getRoutesByUserId(Long userId) {
        return routeRepository.findByUserId(userId);
    }

    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
    }
}
