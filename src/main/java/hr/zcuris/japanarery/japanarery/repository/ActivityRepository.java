package hr.zcuris.japanarery.japanarery.repository;

import hr.zcuris.japanarery.japanarery.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByActivityDateOrderByStartTimeAsc(LocalDate date);

    List<Activity> findByActivityDate(LocalDate activityDate);
}
