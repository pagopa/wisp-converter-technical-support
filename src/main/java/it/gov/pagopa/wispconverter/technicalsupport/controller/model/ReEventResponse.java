package it.gov.pagopa.wispconverter.technicalsupport.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReEventResponse {

    @Schema(example = "2", description = "The number of total flow found on certain payment identifier")
    private int count;

    @Schema(description = "The list of flows executed on certain payment identifier")
    private List<ReEvent> data;
}
