package it.gov.pagopa.wispconverter.technicalsupport.service.model.report;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RPTStatistic {

    private String date;
    private ReportType type;

    private long totalTriggerPrimitivesOnNdp;
    private long totalTriggerPrimitivesOnWisp;

    private long totalRPTCarts;
    private long totalRPTSingles;

    private int completedRPTCarts;
    private int completedRPTSingles;

    private int receiptOkSent;
    private int receiptKoSent;

    private Map<String, Integer> notCompletedTriggeredPrimitivesByStatus;
    private NotCompletedRPTStatistic notCompletedRPTs;

    public RPTStatistic() {
        this.totalTriggerPrimitivesOnNdp = 0;
        this.totalTriggerPrimitivesOnWisp = 0;
        this.totalRPTCarts = 0;
        this.totalRPTSingles = 0;
        this.receiptOkSent = 0;
        this.receiptKoSent = 0;
        this.completedRPTCarts = 0;
        this.completedRPTSingles = 0;
        this.notCompletedTriggeredPrimitivesByStatus = new HashMap<>();
        this.notCompletedRPTs = new NotCompletedRPTStatistic();
    }

    public void addCartOnTotal() {
        this.totalRPTCarts++;
    }

    public void addNoCartOnTotal() {
        this.totalRPTSingles++;
    }

    public void addReceiptOkSent() {
        this.receiptOkSent++;
    }

    public void addReceiptKoSent() {
        this.receiptKoSent++;
    }

    public void addCompletedRPTCarts() {
        this.completedRPTCarts++;
    }

    public void addCompletedRPTSingles() {
        this.completedRPTSingles++;
    }

    public void addNotCompletedTriggeredPrimitives(String status) {
        Integer counter = this.notCompletedTriggeredPrimitivesByStatus.computeIfAbsent(status, k -> 0);
        counter++;
        this.notCompletedTriggeredPrimitivesByStatus.put(status, counter);
    }
}
