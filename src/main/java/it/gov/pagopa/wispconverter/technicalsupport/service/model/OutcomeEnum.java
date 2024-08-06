package it.gov.pagopa.wispconverter.technicalsupport.service.model;

public enum OutcomeEnum {
    SEND,
    SEND_FAILURE,
    RECEIVED,
    RECEIVED_FAILURE,
    NEVER_RECEIVED,
    EXECUTED_INTERNAL_STEP,
    EXCECUTED_INTERNAL_STEP, // TODO for avoid backward compatibility with wrong older RE events, to delete in future steps
}