package it.gov.pagopa.wispconverter.technicalsupport.service.model;

import lombok.*;


@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayloadDto {
  private String payloadRefId;
  private Long payloadLength;
  private String payload;
}
