package it.gov.pagopa.wispconverter.technicalsupport.service.model.report;


import lombok.Data;

@Data
public class NotCompletedRPTStatistic {

    private RPTStatisticDetail rejected;
    private RPTStatisticDetail notSentEndRetry;
    private RPTStatisticDetail sendingOrScheduled;
    private RPTStatisticDetail ongoing;
    private RPTStatisticDetail neverSent;

    public NotCompletedRPTStatistic() {
        this.rejected = new RPTStatisticDetail();
        this.notSentEndRetry = new RPTStatisticDetail();
        this.sendingOrScheduled = new RPTStatisticDetail();
        this.ongoing = new RPTStatisticDetail();
        this.neverSent = new RPTStatisticDetail();
    }
}
