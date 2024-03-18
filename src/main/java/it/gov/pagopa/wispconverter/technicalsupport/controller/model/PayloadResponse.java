package it.gov.pagopa.wispconverter.technicalsupport.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayloadResponse {
  @Schema(example = "2024-03-18_-230539074458600920", description = "Nome del payload")
  private String payloadRefId;
  @Schema(example = "347", description = "Lunghezza del payload")
  private Long payloadLength;
  @Schema(example = "aaAAADdasdsada=", description = "Payload compresso GZIP")
  private String payload;
}
