package it.gov.pagopa.wispconverter.technicalsupport.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RTGroupedByStatusEntity {
    private Integer eventStatusCount;
    private String receiptStatus;
}
