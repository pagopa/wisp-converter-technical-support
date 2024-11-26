package it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFlowDetail {

    @Schema(example = "OK", description = "The status of success or failure related to the step 'trigger primitive' on WISP (i.e. nodoInviaRPT, nodoInviaCarrelloRPT)")
    @JsonProperty("trigger_primitive")
    private String triggerPrimitive;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'redirect' on WISP. It can be null if the previous status is KO")
    @JsonProperty("redirect")
    private String redirect;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'checkPosition' on NDP. It can be null if the previous status is KO")
    @JsonProperty("check_position")
    private String checkPosition;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'activatePaymentNoticeV2' on NDP. It can be null if the previous status is KO")
    @JsonProperty("activate_payment_notice_v2")
    private String activatePaymentNoticeV2;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'closePaymentV2' on NDP. It can be null if the previous status is KO")
    @JsonProperty("close_payment_v2")
    private String closePaymentV2;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'sendPaymentOutcomeV2' on NDP. It can be null if the previous status is KO")
    @JsonProperty("send_payment_outcome_v2")
    private String sendPaymentOutcomeV2;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'rpt-timeout-trigger' on WISP (i.e. if the triggered RPT does not have a following redirect). It can be null if the previous statuses are all OK")
    @JsonProperty("receipt_ko_send_for_rpt_expired")
    private String rptTimeout;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'ecommerce-hang-timeout-trigger' on WISP (i.e. if the user remains too much on ecommerce session opened). It can be null if the previous statuses are all OK")
    @JsonProperty("receipt_ko_send_for_ecommerce_session_expired")
    private String ecommerceTimeout;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'payment-token-timeout-trigger' on WISP (i.e. if the payment token expires during payment processing). It can be null if the previous statuses are all OK")
    @JsonProperty("receipt_ko_send_for_payment_token_expired")
    private String paymentTokenTimeout;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'receipt-ko' on WISP (i.e. the send of a KO paaInviaRT to creditor institution). It can be null if the previous status is KO")
    @JsonProperty("receipt_ko_send")
    private String sendReceiptKO;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'receipt-ko' on WISP (i.e. the send of an OK paaInviaRT to creditor institution). It can be null if the previous status is KO")
    @JsonProperty("receipt_ok_send")
    private String sendReceiptOk;

    @Schema(example = "OK", description = "The status of success or failure related to the step 'receipt-resend-[ok|ko]' on WISP (i.e. the re-send of a paaInviaRT to creditor institution). It can be null if the previous status is OK")
    @JsonProperty("rescheduled_receipt_send")
    private String sendResendReceipt;
}
