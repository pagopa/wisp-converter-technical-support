package it.gov.pagopa.wispconverter.technicalsupport.service.model;

public enum OutcomeEnum {

    INVIATA, INVIATA_KO, RICEVUTA, RICEVUTA_KO, NO_RICEVUTA, CAMBIO_STATO, // TODO to be removed after WISP SOAP Converter update
    SEND,
    SEND_FAILURE,
    RECEIVED,
    RECEIVED_FAILURE,
    NEVER_RECEIVED,
    EXECUTED_INTERNAL_STEP,
}