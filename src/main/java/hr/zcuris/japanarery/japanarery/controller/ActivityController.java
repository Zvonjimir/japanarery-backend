package hr.zcuris.japanarery.japanarery.controller;


import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService service;

    @GetMapping("/today")
    public List<Activity> getTodayActivities() {
        return service.getTodayActivities();
    }

    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable Long id) {
        return service.getActivity(id);
    }

    @GetMapping("/date/{date}")
    public List<Activity> getActivitiesByDate(@PathVariable String date) {
        return service.getActivitiesByDate(date);
    }
}