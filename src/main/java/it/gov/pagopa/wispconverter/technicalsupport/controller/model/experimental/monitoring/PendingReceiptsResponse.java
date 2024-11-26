package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingReceiptsResponse {

    @Schema(description = "The list of payments on which there is a pending receipt")
    @JsonProperty("pending_receipts")
    private List<PendingReceipt> pendingReceipts;
}
