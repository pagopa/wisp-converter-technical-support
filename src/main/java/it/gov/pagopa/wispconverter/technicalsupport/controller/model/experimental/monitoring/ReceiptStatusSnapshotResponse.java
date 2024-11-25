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
public class ReceiptStatusSnapshotResponse {

    @JsonProperty("lower_bound")
    private LocalDateTime lowerBound;

    @JsonProperty("upper_bound")
    private LocalDateTime upperBound;

    @JsonProperty("snapshot")
    private List<ReceiptsStatusSnapshot> snapshot;
}
