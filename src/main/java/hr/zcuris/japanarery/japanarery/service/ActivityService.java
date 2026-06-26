package hr.zcuris.japanarery.japanarery.service;


import hr.zcuris.japanarery.japanarery.dto.ActivityStatsDTO;
import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ActivityStatsDTO getStats() {
        List<Activity> all = repository.findAll();

        List<Activity> paid = all.stream()
                .filter(a -> a.getPrice() != null && a.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .toList();

        BigDecimal total = paid.stream()
                .map(Activity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<LocalDate, BigDecimal> costByDate = all.stream()
                .filter(a -> a.getPrice() != null && a.getActivityDate() != null)
                .collect(Collectors.groupingBy(
                        Activity::getActivityDate,
                        Collectors.reducing(BigDecimal.ZERO, Activity::getPrice, BigDecimal::add)
                ));

        Map<LocalDate, Long> countByDate = all.stream()
                .filter(a -> a.getActivityDate() != null)
                .collect(Collectors.groupingBy(Activity::getActivityDate, Collectors.counting()));

        long totalDays = countByDate.keySet().size();
        long freeDays = countByDate.keySet().stream()
                .filter(d -> costByDate.getOrDefault(d, BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0)
                .count();

        LocalDate mostExpensiveDay = costByDate.entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);

        Map.Entry<LocalDate, Long> busiestEntry = countByDate.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);

        return ActivityStatsDTO.builder()
                .totalActivities(all.size())
                .paidActivities(paid.size())
                .freeActivities(all.size() - paid.size())
                .freePercentage(all.isEmpty() ? 0 : (double)(all.size() - paid.size()) / all.size() * 100)
                .totalCost(total)
                .avgCostPerDay(totalDays == 0 ? BigDecimal.ZERO :
                        total.divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP))
                .avgCostPerPaidActivity(paid.isEmpty() ? BigDecimal.ZERO :
                        total.divide(BigDecimal.valueOf(paid.size()), 2, RoundingMode.HALF_UP))
                .cheapestActivity(paid.stream().map(Activity::getPrice).min(Comparator.naturalOrder()).orElse(null))
                .mostExpensiveActivity(paid.stream().map(Activity::getPrice).max(Comparator.naturalOrder()).orElse(null))
                .mostExpensiveDay(mostExpensiveDay != null ? mostExpensiveDay.toString() : null)
                .busiestDay(busiestEntry != null ? busiestEntry.getKey().toString() : null)
                .busiestDayCount(busiestEntry != null ? busiestEntry.getValue() : 0)
                .freeDays(freeDays)
                .totalDays(totalDays)
                .costByDate(costByDate.entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)))
                .activitiesByDate(countByDate.entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)))
                .build();
    }
}