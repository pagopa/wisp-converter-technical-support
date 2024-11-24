package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowDetail {

    @JsonProperty("trigger_primitive")
    private String triggerPrimitive;

    @JsonProperty("redirect")
    private String redirect;

    @JsonProperty("check_position")
    private String checkPosition;

    @JsonProperty("activate_payment_notice_v2")
    private String activatePaymentNoticeV2;

    @JsonProperty("close_payment_v2")
    private String closePaymentV2;

    @JsonProperty("send_payment_outcome_v2")
    private String sendPaymentOutcomeV2;

    @JsonProperty("receipt_ko_send_for_rpt_expired")
    private String rptTimeout;

    @JsonProperty("receipt_ko_send_for_ecommerce_session_expired")
    private String ecommerceTimeout;

    @JsonProperty("receipt_ko_send_for_payment_token_expired")
    private String paymentTokenTimeout;

    @JsonProperty("receipt_ko_send")
    private String sendReceiptKO;

    @JsonProperty("receipt_ok_send")
    private String sendReceiptOk;

    @JsonProperty("rescheduled_receipt_send")
    private String sendResendReceipt;
}
