package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptsStatusSnapshot {

    @Schema(example = "SENT", description = "The status on which the receipt is found at the moment of the snapshot extraction")
    @JsonProperty("status")
    private String status;

    @Schema(example = "150", description = "The number of receipts that are found in this status at the moment of the snapshot extraction")
    @JsonProperty("count")
    private Integer count;
}
