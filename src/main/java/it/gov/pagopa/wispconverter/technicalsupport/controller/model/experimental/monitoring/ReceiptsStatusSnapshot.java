package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptsStatusSnapshot {

    @JsonProperty("status")
    private String status;

    @JsonProperty("count")
    private Integer count;
}
