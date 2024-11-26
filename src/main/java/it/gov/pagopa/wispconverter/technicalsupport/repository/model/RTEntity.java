package it.gov.pagopa.wispconverter.technicalsupport.repository.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;
import org.springframework.data.annotation.Id;

@Container(containerName = "receipts-rt")
@Data
@ToString(exclude = "rt")
@EqualsAndHashCode(exclude = "rt")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RTEntity {

    @Id
    private String id; // as domainId + iuv + ccp

    @PartitionKey
    private String partitionKey;

    private String domainId;

    private String iuv;

    private String ccp;

    private String sessionId;

    private String receiptStatus;

    private String receiptType;

    private String rt;

    private Long rtTimestamp;
}
