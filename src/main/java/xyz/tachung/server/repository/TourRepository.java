package xyz.tachung.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.tachung.server.Entity.TourPlace;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<TourPlace, Long> {
    Optional<TourPlace> findByTitleAndAddress(String title, String address);


}
