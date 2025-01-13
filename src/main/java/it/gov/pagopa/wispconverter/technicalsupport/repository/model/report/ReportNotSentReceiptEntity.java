package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportNotSentReceiptEntity {

    /**
     * Counter for total not sent receipts, rejected by CI
     */
    @JsonProperty("rejected")
    private ReportNotSentReceiptStatsEntity rejected;

    /**
     * Counter for total not sent receipts, not sent for end retry
     */
    @JsonProperty("not_sent_end_retry")
    private ReportNotSentReceiptStatsEntity notSentEndRetry;

    /**
     * Counter for total not sent receipts, re-scheduled for retry
     */
    @JsonProperty("scheduled")
    private ReportNotSentReceiptStatsEntity scheduled;

    /**
     * Counter for total not sent receipts, for ongoing payment
     */
    @JsonProperty("ongoing")
    private ReportNotSentReceiptStatsEntity ongoing;

    /**
     * Counter for total not sent receipts, never sent to CI
     */
    @JsonProperty("never_sent")
    private ReportNotSentReceiptStatsEntity neverSent;

    public ReportNotSentReceiptEntity() {
        this.rejected = new ReportNotSentReceiptStatsEntity();
        this.notSentEndRetry = new ReportNotSentReceiptStatsEntity();
        this.scheduled = new ReportNotSentReceiptStatsEntity();
        this.ongoing = new ReportNotSentReceiptStatsEntity();
        this.neverSent = new ReportNotSentReceiptStatsEntity();
    }

    public void merge(ReportNotSentReceiptEntity other) {
        this.rejected.merge(other.rejected);
        this.notSentEndRetry.merge(other.notSentEndRetry);
        this.scheduled.merge(other.scheduled);
        this.ongoing.merge(other.ongoing);
        this.neverSent.merge(other.neverSent);
    }
}
