package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingReceiptsRequest {

    @Schema(example = "2024-01-01T12:00:00", description = "Lower limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("lower_bound_date")
    private LocalDateTime lowerBoundDate;

    @Schema(example = "2024-01-01T12:00:00", description = "Upper limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("upper_bound_date")
    private LocalDateTime upperBoundDate;

    @JsonProperty("creditor_institution")
    private String creditorInstitution;

    @JsonProperty("iuvs")
    private Set<String> iuvs;
}
