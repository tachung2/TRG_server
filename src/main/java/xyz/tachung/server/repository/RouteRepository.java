package xyz.tachung.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.tachung.server.Entity.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByUserId(Long userId);
    Optional<Route> findByUserIdAndRoutename(Long userId, String routename);
}
