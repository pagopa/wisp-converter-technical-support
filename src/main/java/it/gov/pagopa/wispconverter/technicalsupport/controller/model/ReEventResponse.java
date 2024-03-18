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

    @Schema(description = "Totale dei record trovati", example = "100")
    private int count;

    @Schema(description = "Lista dei record trovati")
    private List<ReEvent> data;
}
