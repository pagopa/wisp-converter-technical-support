package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class NotCompletedTriggerPrimitivesEntity {

    /**
     * Counter for trigger primitives not completed, closed in KO status by process rpt_timeout_trigger
     */
    @JsonProperty("rpt_timeout_trigger")
    private Integer rptTimeoutTrigger;

    /**
     * Counter for trigger primitives not completed, closed in KO status by process redirect
     */
    @JsonProperty("redirect")
    private Integer redirect;

    /**
     * Counter for trigger primitives not completed, closed in KO status by process receipt_ko
     */
    @JsonProperty("receipt_ko")
    private Integer receiptKo;

    /**
     * Counter for trigger primitives not completed, closed in KO status by process payment_token_timeout_trigger
     */
    @JsonProperty("payment_token_timeout_trigger")
    private Integer paymentTokenTimeoutTrigger;

    /**
     * Counter for trigger primitives not completed, closed in KO status by process ecommerce_hang_timeout_trigger
     */
    @JsonProperty("ecommerce_hang_timeout_trigger")
    private Integer ecommerceHangTimeoutTrigger;

    /**
     * Counter for trigger primitives not completed, closed in KO status by some undefined process
     */
    @JsonProperty("no_state")
    private Integer noState;

    public NotCompletedTriggerPrimitivesEntity() {
        this.rptTimeoutTrigger = 0;
        this.redirect = 0;
        this.receiptKo = 0;
        this.paymentTokenTimeoutTrigger = 0;
        this.ecommerceHangTimeoutTrigger = 0;
        this.noState = 0;
    }

    public void merge(NotCompletedTriggerPrimitivesEntity other) {
        this.rptTimeoutTrigger += other.rptTimeoutTrigger;
        this.redirect += other.redirect;
        this.receiptKo += other.receiptKo;
        this.paymentTokenTimeoutTrigger += other.paymentTokenTimeoutTrigger;
        this.ecommerceHangTimeoutTrigger += other.ecommerceHangTimeoutTrigger;
        this.noState += other.noState;
    }
}
