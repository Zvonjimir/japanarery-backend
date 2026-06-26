package hr.zcuris.japanarery.japanarery.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ActivityStatsDTO {

    private long totalActivities;
    private long paidActivities;
    private long freeActivities;
    private double freePercentage;

    private BigDecimal totalCost;
    private BigDecimal avgCostPerDay;
    private BigDecimal avgCostPerPaidActivity;
    private BigDecimal cheapestActivity;
    private BigDecimal mostExpensiveActivity;

    private String mostExpensiveDay;
    private String busiestDay;
    private long busiestDayCount;

    private long freeDays;
    private long totalDays;

    private Map<String, BigDecimal> costByDate;
    private Map<String, Long> activitiesByDate;
}