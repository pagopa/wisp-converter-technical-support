package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReEventRepository extends CosmosRepository<ReEventEntity, String> {

    @Query("SELECT * " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.sessionId = @sessionId " +
            "ORDER BY c._ts ASC")
    Set<ReEventEntity> findBySessionId(@Param("dateFrom") String dateFrom,
                                       @Param("dateTo") String dateTo,
                                       @Param("sessionId") String sessionId);


    @Query("SELECT DISTINCT VALUE c.sessionId " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.iuv = @iuv " +
            "AND c.domainId = @organizationId " +
            "AND IS_DEFINED(c.sessionId) AND c.sessionId != null")
    Set<String> findSessionIdByIuvAndDomainId(@Param("dateFrom") String dateFrom,
                                              @Param("dateTo") String dateTo,
                                              @Param("organizationId") String organizationId,
                                              @Param("iuv") String iuv);


    @Query("SELECT DISTINCT VALUE c.sessionId " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.noticeNumber = @noticeNumber " +
            "AND c.domainId = @organizationId " +
            "AND IS_DEFINED(c.sessionId) AND c.sessionId != null")
    Set<String> findSessionIdByNoticeNumberAndDomainId(@Param("dateFrom") String dateFrom,
                                                       @Param("dateTo") String dateTo,
                                                       @Param("organizationId") String organizationId,
                                                       @Param("noticeNumber") String noticeNumber);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.sessionId = @sessionId " +
            "AND c.status = 'COMMUNICATION_WITH_CREDITOR_INSTITUTION_PROCESSED' " +
            "AND c.businessProcess IN ('rpt-timeout-trigger', 'redirect', 'ecommerce-hang-timeout-trigger', 'payment-token-timeout-trigger', 'receipt-ko', 'receipt-ok')")
    List<ReEventEntity> findRtTriggerRelatedEventBySessionId(@Param("noticeNumber") String sessionId);
}