package it.gov.pagopa.wispconverter.technicalsupport.repository.model.re;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Container(containerName = "re")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReEventEntity {

    /**
     * The identifier of the single event.
     */
    @Id
    @GeneratedValue
    private String id;

    /**
     * The partition key that refers to the single partition on which the event will be stored.
     */
    @PartitionKey
    private String partitionKey;

    private String operationId;
    private Instant insertedTimestamp;
    private String businessProcess;
    private String eventCategory;
    private String status;
    private String outcome;
    private String httpMethod;
    private String httpUri;
    private Integer httpStatusCode;
    private Long executionTimeMs;
    private String requestHeaders;
    private String responseHeaders;
    private String requestPayload;
    private String responsePayload;
    private String operationErrorCode;
    private String operationErrorLine;
    private String operationErrorDetail;
    private String sessionId;
    private String cartId;
    private String iuv;
    private String noticeNumber;
    private String domainId;
    private String ccp;
    private String psp;
    private String station;
    private String channel;
    private String paymentToken;
    private String info;
    private Instant _ts;

    public Instant getTimestamp() {
        return this._ts;
    }
}
