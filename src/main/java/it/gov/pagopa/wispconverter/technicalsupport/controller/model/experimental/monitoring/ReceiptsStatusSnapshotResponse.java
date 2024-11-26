package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptsStatusSnapshotResponse {

    @Schema(example = "2024-01-01T12:00:00", description = "The lower limit of the date slot in 'Europe/Rome' timezone, used as delimiter for the search time (in yyyy-MM-ddThh:mm:ss)")
    @JsonProperty("lower_bound_date")
    private LocalDateTime lowerBoundDate;

    @Schema(example = "2024-01-01T12:00:00", description = "The upper limit of the date slot in 'Europe/Rome' timezone, used as delimiter for the search time (in yyyy-MM-ddThh:mm:ss)")
    @JsonProperty("upper_bound_date")
    private LocalDateTime upperBoundDate;

    @Schema(description = "The number of receipts classified by their status, extracted in the temporal snapshot")
    @JsonProperty("snapshot")
    private List<ReceiptsStatusSnapshot> snapshot;
}
