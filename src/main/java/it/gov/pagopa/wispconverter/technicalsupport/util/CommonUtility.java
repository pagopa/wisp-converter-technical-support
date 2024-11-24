package it.gov.pagopa.wispconverter.technicalsupport.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
}
