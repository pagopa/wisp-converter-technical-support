package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Container(containerName = "report")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
}
