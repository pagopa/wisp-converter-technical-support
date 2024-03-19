package it.gov.pagopa.wispconverter.technicalsupport.service;

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
