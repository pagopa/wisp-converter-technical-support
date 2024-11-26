package it.gov.pagopa.wispconverter.technicalsupport.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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
}
