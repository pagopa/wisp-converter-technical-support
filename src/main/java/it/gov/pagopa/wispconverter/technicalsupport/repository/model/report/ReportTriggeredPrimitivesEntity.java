package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportTriggeredPrimitivesEntity {

    /**
     * Counter for all nodoInviaCarrelloRPT handled by D-WISP
     */
    @JsonProperty("total_carts")
    private Long totalCarts;

    /**
     * Counter for all nodoInviaRPT handled by D-WISP
     */
    @JsonProperty("total_no_carts")
    private Long totalNoCarts;

    /**
     * Counter for completed nodoInviaCarrelloRPT
     */
    @JsonProperty("carts_completed")
    private Integer cartsCompleted;

    /**
     * Counter for completed nodoInviaRPT
     */
    @JsonProperty("no_carts_completed")
    private Integer noCartsCompleted;

    /**
     * Counter for all not completed triggered primitives, catalogued by error
     */
    @JsonProperty("all_not_completed")
    private NotCompletedTriggerPrimitivesEntity allNotCompleted;

    public ReportTriggeredPrimitivesEntity() {
        this.totalCarts = 0L;
        this.totalNoCarts = 0L;
        this.cartsCompleted = 0;
        this.noCartsCompleted = 0;
        this.allNotCompleted = new NotCompletedTriggerPrimitivesEntity();
    }

    public void merge(ReportTriggeredPrimitivesEntity other) {

        this.totalCarts += other.totalCarts;
        this.totalNoCarts += other.totalNoCarts;
        this.cartsCompleted += other.cartsCompleted;
        this.noCartsCompleted += other.noCartsCompleted;
        this.allNotCompleted.merge(other.allNotCompleted);
    }
}
