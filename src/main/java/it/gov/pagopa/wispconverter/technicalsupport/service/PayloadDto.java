package it.gov.pagopa.wispconverter.technicalsupport.service;

import lombok.*;

import java.time.LocalDateTime;

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
