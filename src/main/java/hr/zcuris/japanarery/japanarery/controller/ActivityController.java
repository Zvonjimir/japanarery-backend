package hr.zcuris.japanarery.japanarery.controller;

import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.service.ActivityService;
import hr.zcuris.japanarery.japanarery.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService service;
    private final CloudinaryService cloudinaryService;

    // ─── READ ────────────────────────────────────────────

    @GetMapping("/today")
    public ResponseEntity<List<Activity>> getTodayActivities() {
        log.info("GET /today — fetching today's activities");
        List<Activity> activities = service.getTodayActivities();
        log.info("GET /today — returned {} activities", activities.size());
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.info("GET /{} — fetching activity", id);
        Activity activity = service.getActivity(id);
        log.info("GET /{} — found: {}", id, activity.getActivityName());
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Activity>> getActivitiesByDate(@PathVariable String date) {
        log.info("GET /date/{} — fetching activities", date);
        List<Activity> activities = service.getActivitiesByDate(date);
        log.info("GET /date/{} — returned {} activities", date, activities.size());
        return ResponseEntity.ok(activities);
    }

    // ─── CREATE ──────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Activity> createActivity(
            @RequestPart("activity") Activity activity,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        log.info("POST / — creating activity: {}", activity.getActivityName());

        if (image != null && !image.isEmpty()) {
            log.info("POST / — uploading image: {} ({} bytes)", image.getOriginalFilename(), image.getSize());
            String url = cloudinaryService.uploadImage(image);
            activity.setImageUrl(url);
            log.info("POST / — image uploaded: {}", url);
        } else {
            log.info("POST / — no image provided");
        }

        Activity created = service.createActivity(activity);
        log.info("POST / — activity created with id: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    // ─── UPDATE ──────────────────────────────────────────

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Activity> updateActivity(
            @PathVariable Long id,
            @RequestPart("activity") Activity activity,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        log.info("PUT /{} — updating activity", id);

        if (image != null && !image.isEmpty()) {
            log.info("PUT /{} — uploading new image: {} ({} bytes)", id, image.getOriginalFilename(), image.getSize());
            String url = cloudinaryService.uploadImage(image);
            activity.setImageUrl(url);
            log.info("PUT /{} — new image uploaded: {}", id, url);
        } else {
            log.info("PUT /{} — no new image provided", id);
        }

        Activity updated = service.updateActivity(id, activity);
        log.info("PUT /{} — activity updated: {}", id, updated.getActivityName());
        return ResponseEntity.ok(updated);
    }

    // ─── DELETE ──────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) throws IOException {
        log.info("DELETE /{} — deleting activity", id);

        Activity activity = service.getActivity(id);

        if (activity.getImageUrl() != null) {
            log.info("DELETE /{} — deleting image from Cloudinary: {}", id, activity.getImageUrl());
            cloudinaryService.deleteImage(activity.getImageUrl());
            log.info("DELETE /{} — image deleted from Cloudinary", id);
        } else {
            log.info("DELETE /{} — no image to delete", id);
        }

        service.deleteActivity(id);
        log.info("DELETE /{} — activity deleted", id);
        return ResponseEntity.noContent().build();
    }
}