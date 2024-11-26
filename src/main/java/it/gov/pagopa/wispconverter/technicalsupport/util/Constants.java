package it.gov.pagopa.wispconverter.technicalsupport.util;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class Constants {

    public static final String PATTERN_FORMAT = "yyyy-MM-dd";
    public static final String TIMESTAMP_PATTERN_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";

    public static final String ORGANIZATION = "organizationId";
    public static final String IUV = "iuv";
    public static final String NOTICE_NUMBER = "noticeNumber";
    public static final String SESSION_ID = "sessionId";

    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String SHOW_DETAILS = "showDetails";
    public static final String SHOW_COMPACT_FORM = "showCompactForm";
    public static final String SHOW_PAYLOADS = "showPayloads";

    public static final String STEP_TRIGGER_PRIMITIVE_NODOINVIARPT = "nodoInviaRPT";
    public static final String STEP_TRIGGER_PRIMITIVE_NODOINVIACARRELLORPT = "nodoInviaCarrelloRPT";
    public static final String STEP_REDIRECT = "redirect";
    public static final String STEP_CHECKPOSITION = "checkPosition";
    public static final String STEP_ACTIVATEPAYMENTNOTICE = "activatePaymentNoticeV2";
    public static final String STEP_CLOSEPAYMENT = "closePayment-v2";
    public static final String STEP_SENDPAYMENTOUTCOME = "sendPaymentOutcomeV2";
    public static final String STEP_RPTTIMEOUTTRIGGER = "rpt-timeout-trigger";
    public static final String STEP_ECOMMERCETIMEOUTTRIGGER = "ecommerce-hang-timeout-trigger";
    public static final String STEP_PAYMENTTOKENTIMEOUTTRIGGER = "payment-token-timeout-trigger";
    public static final String STEP_RECEIPTOK = "receipt-ok";
    public static final String STEP_RECEIPTKO = "receipt-ko";
    public static final String STEP_RESEND_RECEIPT = "resend-rt";


    public static final Set<String> ACCEPTED_TRIGGER_PRIMITIVE = Set.of(STEP_TRIGGER_PRIMITIVE_NODOINVIARPT, STEP_TRIGGER_PRIMITIVE_NODOINVIACARRELLORPT);

    public static final String SUBSCRIPTION_KEY_PATTERN = "\"?Ocp-Apim-Subscription-Key\"?: ?\\[(\\S+ ?\\S+|\\S+, \\S+)]";


}
