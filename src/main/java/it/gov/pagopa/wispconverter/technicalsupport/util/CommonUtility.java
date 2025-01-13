package it.gov.pagopa.wispconverter.technicalsupport.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.zip.GZIPInputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtility {


    /**
     * @param value value to deNullify.
     * @return return empty string if value is null
     */
    public static String deNull(String value) {
        return Optional.ofNullable(value).orElse("");
    }

    /**
     * @param value value to deNullify.
     * @return return empty string if value is null
     */
    public static String deNull(Object value) {
        return Optional.ofNullable(value).orElse("").toString();
    }

    /**
     * @param value value to nullify.
     * @return return null string if value is null or empty
     */
    public static String nullify(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    /**
     * @param value value to deNullify.
     * @return return false if value is null
     */
    public static Boolean deNull(Boolean value) {
        return Optional.ofNullable(value).orElse(false);
    }

    /**
     * @param headers header of the CSV file
     * @param rows    data of the CSV file
     * @return byte array of the CSV using commas (;) as separator
     */
    public static byte[] createCsv(List<String> headers, List<List<String>> rows) {
        var csv = new StringBuilder();
        csv.append(String.join(";", headers));
        rows.forEach(row -> csv.append(System.lineSeparator()).append(String.join(";", row)));
        return csv.toString().getBytes();
    }

    public static long getTimelapse(long startTime) {
        return Calendar.getInstance().getTimeInMillis() - startTime;
    }

    public static String partitionKeyFromInstant(LocalDate insertedTimestamp) {
        return insertedTimestamp == null ? null : DateTimeFormatter
                .ofPattern(Constants.PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault())
                .format(insertedTimestamp);
    }

    public static String timestampFromInstant(LocalDateTime insertedTimestamp) {
        return insertedTimestamp == null ? null : DateTimeFormatter
                .ofPattern(Constants.TIMESTAMP_PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault())
                .format(insertedTimestamp);
    }

    public static String decodeBase64(String base64String) {
        String decodedValue;
        if (base64String == null) {
            decodedValue = null;
        } else if (base64String.isBlank()) {
            decodedValue = "";
        } else {
            decodedValue = new String(Base64.getDecoder().decode(base64String));
        }
        return decodedValue;
    }

    public static String decompressGZip(String gzipContent) {
        String result;
        if (gzipContent == null || gzipContent.isEmpty()) {
            result = "";

        } else {

            byte[] compressedData = Base64.getDecoder().decode(gzipContent);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);

            try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {

                byte[] buffer = new byte[1024];
                StringBuilder output = new StringBuilder();
                int bytesRead;
                while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                    output.append(new String(buffer, 0, bytesRead));
                }
                result = output.toString();

            } catch (IOException e) {
                result = null;
            }
        }
        return result;
    }

    public static float safeDivide(float denominator, float numerator) {
        float value = 0;
        if (denominator != 0) {
            value = numerator / denominator;
        }
        return value;
    }

    public static String getYesterday(String date) {

        LocalDate passedDate = LocalDate.parse(date);
        return passedDate.minusDays(1).toString();
    }

    public static List<String> getWeekInDate(String date) {

        // Convert the string in format "yyyy-MM-dd" and calculate the first day of the week (monday)
        LocalDate passedDate = LocalDate.parse(date);
        LocalDate weekStart = passedDate.minusDays((long) passedDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());

        // Extract the days of the week
        List<String> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(weekStart.plusDays(i).toString());
        }

        return weekDates;
    }

    public static List<String> getMonthInDate(String date) {

        // Convert the input string in format "yyyy-MM-dd" and calculate the first and last days of the month for the provided date
        LocalDate passedDate = LocalDate.parse(date);
        LocalDate firstDayOfMonth = passedDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = passedDate.with(TemporalAdjusters.lastDayOfMonth());

        // Collect all the dates of the month
        List<String> monthDates = new ArrayList<>();
        LocalDate currentDate = firstDayOfMonth;
        while (!currentDate.isAfter(lastDayOfMonth)) {
            monthDates.add(currentDate.toString());
            currentDate = currentDate.plusDays(1);
        }

        return monthDates;
    }
}
