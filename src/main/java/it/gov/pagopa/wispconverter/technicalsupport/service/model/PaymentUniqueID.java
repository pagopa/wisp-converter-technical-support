package it.gov.pagopa.wispconverter.technicalsupport.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentUniqueID {

    private String domainId;
    private String code;
    private String ccp;
}
