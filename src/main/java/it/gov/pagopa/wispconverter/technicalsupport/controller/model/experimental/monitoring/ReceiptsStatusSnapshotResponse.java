package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("lower_bound_date")
    private LocalDateTime lowerBoundDate;

    @JsonProperty("upper_bound_date")
    private LocalDateTime upperBoundDate;

    @JsonProperty("snapshot")
    private List<ReceiptsStatusSnapshot> snapshot;
}
