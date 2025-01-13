package it.gov.pagopa.wispconverter.technicalsupport.service.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Data
public class RPTStatisticDetail {

    private int receiptOkCount;
    private int receiptKoCount;
    private List<String> receiptsIds;

    public RPTStatisticDetail() {
        this.receiptOkCount = 0;
        this.receiptKoCount = 0;
        this.receiptsIds = new LinkedList<>();
    }

    public void addAsOkReceipt(String receiptId) {
        this.receiptOkCount++;
        this.receiptsIds.add(receiptId);
    }

    public void addAsKoReceipt(String receiptId) {
        this.receiptOkCount++;
        this.receiptsIds.add(receiptId);
    }
}
