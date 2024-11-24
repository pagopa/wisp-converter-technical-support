package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental;

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
public class PaymentFlowMetadata {

    @Schema(description = "")
    @JsonProperty("search_filters")
    private PaymentFlowSearchFilterMetadata searchFilters;

    @Schema(description = "")
    @JsonProperty("show_internal_statuses_details")
    private boolean showInternalStatusesDetails;

    @Schema(description = "")
    @JsonProperty("show_data_in_compact_form")
    private boolean showDataInCompactForm;

    @Schema(description = "")
    @JsonProperty("show_payloads_in_interface_statuses")
    private boolean showPayloadsInInterfaceStatuses;

    @Schema(description = "Number of total flow executed on certain payment position", example = "2")
    @JsonProperty("number_of_flows_found")
    private int numberOfFlowsFound;
}
