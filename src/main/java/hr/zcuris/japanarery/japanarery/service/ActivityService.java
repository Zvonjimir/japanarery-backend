package hr.zcuris.japanarery.japanarery.service;


import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository repository;

    public List<Activity> getTodayActivities() {
        return repository.findByActivityDateOrderByStartTimeAsc(LocalDate.now());
    }

    public Activity getActivity(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public List<Activity> getActivitiesByDate(String date) {
        return repository.findByActivityDate(LocalDate.parse(date));
    }

    public Activity createActivity(Activity activity) {
        return repository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updated) {
        Activity existing = getActivity(id);
        existing.setActivityName(updated.getActivityName());
        existing.setPrice(updated.getPrice());
        existing.setActivityDate(updated.getActivityDate());
        existing.setStartTime(updated.getStartTime());
        existing.setDescription(updated.getDescription());
        existing.setAdditionalNotes(updated.getAdditionalNotes());
        existing.setGoogleMapsLink(updated.getGoogleMapsLink());
        if (updated.getImageUrl() != null) {
            existing.setImageUrl(updated.getImageUrl());
        }
        return repository.save(existing);
    }

    public void deleteActivity(Long id) {
        repository.deleteById(id);
    }
}