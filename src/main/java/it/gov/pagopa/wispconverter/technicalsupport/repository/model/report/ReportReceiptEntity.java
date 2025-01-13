package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReportReceiptEntity {

    /**
     * Statistics about OK or KO receipt correctly sent
     */
    @JsonProperty("completed")
    private ReportSentReceiptEntity completed;

    /**
     * Statistics about OK or KO receipt not correctly sent
     */
    @JsonProperty("not_completed")
    private ReportNotSentReceiptEntity notCompleted;
}
