package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Container(containerName = "reports")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportEntity {

    @Id
    private String id;

    @PartitionKey
    private String date;

    /**
     * Statistics about trigger primitives
     */
    private ReportPaymentEntity payments;

    /**
     * Statistics about handled receipts
     */
    private ReportReceiptEntity receipts;

    public ReportEntity() {
        this.payments = new ReportPaymentEntity();
        this.receipts = new ReportReceiptEntity();
    }

    public void merge(ReportEntity other) {
        this.payments.merge(other.payments);
        this.receipts.merge(other.receipts);
    }
}
