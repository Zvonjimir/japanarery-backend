package hr.zcuris.japanarery.japanarery.config;

import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ActivityRepository repository;

    @Override
    public void run(String... args) {

        repository.deleteAll();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Activity> activities = List.of(

                /* ===================================== */
                /* YESTERDAY */
                /* ===================================== */

                Activity.builder()
                        .activityName("Asakusa Temple Visit")
                        .price(BigDecimal.ZERO)
                        .activityDate(yesterday)
                        .startTime(LocalTime.of(8, 30))
                        .description("Morning walk through Senso-ji temple")
                        .additionalNotes("Perfect for photos early morning")
                        .googleMapsLink("https://maps.google.com/?q=Sensoji+Temple+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1528360983277-13d401cdc186")
                        .build(),

                Activity.builder()
                        .activityName("Akihabara Arcade Tour")
                        .price(BigDecimal.valueOf(15))
                        .activityDate(yesterday)
                        .startTime(LocalTime.of(13, 0))
                        .description("Retro arcades and anime shopping")
                        .additionalNotes("Try rhythm games")
                        .googleMapsLink("https://maps.google.com/?q=Akihabara+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1545569341-9eb8b30979d9")
                        .build(),

                Activity.builder()
                        .activityName("Shinjuku Night Walk")
                        .price(BigDecimal.ZERO)
                        .activityDate(yesterday)
                        .startTime(LocalTime.of(21, 0))
                        .description("Explore neon streets and nightlife")
                        .additionalNotes("Kabukicho gets crowded")
                        .googleMapsLink("https://maps.google.com/?q=Shinjuku+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1542051841857-5f90071e7989")
                        .build(),

                /* ===================================== */
                /* TODAY */
                /* ===================================== */

                Activity.builder()
                        .activityName("Shibuya Crossing")
                        .price(BigDecimal.ZERO)
                        .activityDate(today)
                        .startTime(LocalTime.of(9, 0))
                        .description("Explore the busiest crossing in Tokyo")
                        .additionalNotes("Take photos from Starbucks view")
                        .googleMapsLink("https://maps.google.com/?q=Shibuya+Crossing+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1540959733332-eab4deabeeaf")
                        .build(),

                Activity.builder()
                        .activityName("Tsukiji Sushi Breakfast")
                        .price(BigDecimal.valueOf(22))
                        .activityDate(today)
                        .startTime(LocalTime.of(10, 30))
                        .description("Fresh sushi breakfast experience")
                        .additionalNotes("Arrive hungry")
                        .googleMapsLink("https://maps.google.com/?q=Tsukiji+Market+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836")
                        .build(),

                Activity.builder()
                        .activityName("Ichiran Ramen")
                        .price(BigDecimal.valueOf(18))
                        .activityDate(today)
                        .startTime(LocalTime.of(12, 30))
                        .description("Lunch at famous ramen restaurant")
                        .additionalNotes("Try extra egg")
                        .googleMapsLink("https://maps.google.com/?q=Ichiran+Shibuya")
                        .imageUrl("https://images.unsplash.com/photo-1557872943-16a5ac26437e")
                        .build(),

                Activity.builder()
                        .activityName("Meiji Shrine")
                        .price(BigDecimal.ZERO)
                        .activityDate(today)
                        .startTime(LocalTime.of(15, 0))
                        .description("Peaceful shrine surrounded by forest")
                        .additionalNotes("Very calm atmosphere")
                        .googleMapsLink("https://maps.google.com/?q=Meiji+Shrine+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1503899036084-c55cdd92da26")
                        .build(),

                Activity.builder()
                        .activityName("Tokyo Tower")
                        .price(BigDecimal.valueOf(25))
                        .activityDate(today)
                        .startTime(LocalTime.of(18, 0))
                        .description("Night view over Tokyo")
                        .additionalNotes("Best during sunset")
                        .googleMapsLink("https://maps.google.com/?q=Tokyo+Tower")
                        .imageUrl("https://images.unsplash.com/photo-1536098561742-ca998e48cbcc")
                        .build(),

                Activity.builder()
                        .activityName("Golden Gai Drinks")
                        .price(BigDecimal.valueOf(35))
                        .activityDate(today)
                        .startTime(LocalTime.of(22, 0))
                        .description("Tiny bars and Tokyo nightlife")
                        .additionalNotes("Cash only in some bars")
                        .googleMapsLink("https://maps.google.com/?q=Golden+Gai+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1513407030348-c983a97b98d8")
                        .build(),

                /* ===================================== */
                /* TOMORROW */
                /* ===================================== */

                Activity.builder()
                        .activityName("TeamLab Planets")
                        .price(BigDecimal.valueOf(32))
                        .activityDate(tomorrow)
                        .startTime(LocalTime.of(11, 0))
                        .description("Immersive digital art museum")
                        .additionalNotes("Wear shorts, water floor inside")
                        .googleMapsLink("https://maps.google.com/?q=teamLab+Planets+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1516321318423-f06f85e504b3")
                        .build(),

                Activity.builder()
                        .activityName("Harajuku Street Shopping")
                        .price(BigDecimal.ZERO)
                        .activityDate(tomorrow)
                        .startTime(LocalTime.of(14, 30))
                        .description("Fashion, crepes and youth culture")
                        .additionalNotes("Visit Takeshita Street")
                        .googleMapsLink("https://maps.google.com/?q=Harajuku+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1526481280695-3c4691f38f36")
                        .build(),

                Activity.builder()
                        .activityName("Odaiba Evening")
                        .price(BigDecimal.valueOf(12))
                        .activityDate(tomorrow)
                        .startTime(LocalTime.of(19, 0))
                        .description("Tokyo Bay lights and Gundam statue")
                        .additionalNotes("Great sunset location")
                        .googleMapsLink("https://maps.google.com/?q=Odaiba+Tokyo")
                        .imageUrl("https://images.unsplash.com/photo-1492571350019-22de08371fd3")
                        .build()
        );

        repository.saveAll(activities);

        System.out.println("Activities seeded: " + activities.size());
    }
}