package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportPaymentEntity {

    /**
     * Counter for total nodoInviaRPT and nodoInviaCarrelloRPT handled by NdP
     */
    @JsonProperty("total_on_ndp")
    private Long totalOnNdp;

    /**
     * Counter for total nodoInviaRPT and nodoInviaCarrelloRPT handled by D-WISP
     */
    @JsonProperty("total_on_wisp")
    private Long totalOnWisp;

    /**
     * Statistics for cataloguing success and failure on nodoInviaRPT and nodoInviaCarrelloRPT handled by D-WISP
     */
    @JsonProperty("trigger_primitives")
    private ReportTriggeredPrimitivesEntity triggerPrimitives;

    public ReportPaymentEntity() {
        this.totalOnNdp = 0L;
        this.totalOnWisp = 0L;
        this.triggerPrimitives = new ReportTriggeredPrimitivesEntity();
    }

    public void merge(ReportPaymentEntity other) {
        this.totalOnNdp += other.totalOnNdp;
        this.totalOnWisp += other.totalOnWisp;
        this.triggerPrimitives.merge(other.triggerPrimitives);
    }
}
