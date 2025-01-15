package it.gov.pagopa.wispconverter.technicalsupport.service.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class RPTStatisticDetail {

    private int receiptOkCount;
    private int receiptKoCount;
    private Set<String> receiptsIds;

    public RPTStatisticDetail() {
        this.receiptOkCount = 0;
        this.receiptKoCount = 0;
        this.receiptsIds = new HashSet<>();
    }

    public void addAsOkReceipt(String receiptId) {
        this.receiptOkCount++;
        this.receiptsIds.add(receiptId);
    }

    public void addAsKoReceipt(String receiptId) {
        this.receiptKoCount++;
        this.receiptsIds.add(receiptId);
    }
}
