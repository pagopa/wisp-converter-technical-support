package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptsStatus {

    @Schema(example = "012345678901", description = "The identifier of the creditor institution")
    @JsonProperty("domain_id")
    private String domainId;

    @Schema(example = "001924757343397669", description = "The IUV code of the payment")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(example = "001924757343397669-00001", description = "The payment context code (aka CCP) of the payment tentative")
    @JsonProperty("ccp")
    private String ccp;

    @Schema(example = "KO", description = "The type of the receipt (OK or KO)")
    @JsonProperty("type")
    private String type;

    @Schema(example = "SENT", description = "The sending status of the receipt")
    @JsonProperty("status")
    private String status;

    @Schema(example = "2024-01-01T12:00:00", description = "The datetime of the last update made on the receipt")
    @JsonProperty("last_update")
    private Instant lastUpdate;
}
