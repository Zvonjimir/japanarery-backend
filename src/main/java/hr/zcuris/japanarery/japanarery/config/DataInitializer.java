package hr.zcuris.japanarery.japanarery.config;

import hr.zcuris.japanarery.japanarery.entity.Activity;
import hr.zcuris.japanarery.japanarery.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ActivityRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.deleteAll();

        List<Activity> activities = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource("japan_activities.xlsx");
        try (InputStream is = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Preskoči header row (redak 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Activity activity = Activity.builder()
                        .activityName(getString(row, 1))
                        .price(getBigDecimal(row, 2))
                        .activityDate(getLocalDate(row, 3))
                        .startTime(getLocalTime(row, 4))
                        .description(getString(row, 5))
                        .additionalNotes(getString(row, 6))
                        .googleMapsLink(getString(row, 7))
                        .imageUrl(getString(row, 8))
                        .build();

                activities.add(activity);
            }
        }

        repository.saveAll(activities);
        log.info("Activities seeded from Excel: {}", activities.size());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().isBlank() ? null : cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default      -> null;
        };
    }

    private BigDecimal getBigDecimal(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private LocalDate getLocalDate(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
            return cell.getLocalDateTimeCellValue().toLocalDate();
        // String fallback: "2026-07-13"
        return LocalDate.parse(cell.getStringCellValue().trim());
    }

    private LocalTime getLocalTime(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
            return cell.getLocalDateTimeCellValue().toLocalTime();
        // String fallback: "08:00"
        return LocalTime.parse(cell.getStringCellValue().trim());
    }
}