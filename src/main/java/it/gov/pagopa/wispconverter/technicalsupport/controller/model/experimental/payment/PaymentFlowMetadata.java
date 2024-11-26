package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;

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

    @Schema(description = "The payment identifier information used as a search filter")
    @JsonProperty("search_filters")
    private PaymentFlowSearchFilterMetadata searchFilters;

    @Schema(example = "false", description = "The flag that permits to enable the display of internal states of the entire process")
    @JsonProperty("show_internal_statuses_details")
    private boolean showInternalStatusesDetails;

    @Schema(example = "true", description = "The flag that permits to enable the display of data in a compact form, reducing to essential information")
    @JsonProperty("show_data_in_compact_form")
    private boolean showDataInCompactForm;

    @Schema(example = "false", description = "The flag that permits to enable the display of request and response payloads in INTERFACE events")
    @JsonProperty("show_payloads_in_interface_statuses")
    private boolean showPayloadsInInterfaceStatuses;

    @Schema(example = "2", description = "The number of total flow found on certain payment identifier")
    @JsonProperty("number_of_flows_found")
    private int numberOfFlowsFound;
}
