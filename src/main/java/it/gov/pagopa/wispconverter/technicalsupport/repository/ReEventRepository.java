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


    @Query("select * from c where c.partitionKey >= @dateFrom and c.partitionKey <= @dateTo and c.operationId = @operationId")
    List<ReEventEntity> findByOperationId(@Param("dateFrom") String dateFrom,
                                          @Param("dateTo") String dateTo,
                                          @Param("operationId") String operationId);


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

    @Query("SELECT DISTINCT VALUE c.noticeNumber " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND ARRAY_CONTAINS(@sessionIds, c.sessionId) " +
            "AND IS_DEFINED(c.noticeNumber) AND c.noticeNumber != null")
    Set<String> findNoticeNumberBySessionId(@Param("dateFrom") String dateFrom,
                                            @Param("dateTo") String dateTo,
                                            @Param("sessionIds") Set<String> sessionId);

    @Query("SELECT DISTINCT VALUE c.domainId " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.sessionId = @sessionId " +
            "AND IS_DEFINED(c.domainId) AND c.domainId != null")
    List<String> findDomainIdBySessionId(@Param("dateFrom") String dateFrom,
                                         @Param("dateTo") String dateTo,
                                         @Param("sessionId") String sessionId);

    @Query("SELECT DISTINCT VALUE c.paymentToken " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND ARRAY_CONTAINS(@noticeNumbers, c.noticeNumber) " +
            "AND c.domainId = @organizationId " +
            "AND IS_DEFINED(c.paymentToken) AND c.paymentToken != null")
    Set<String> findPaymentTokenByNoticeNumber(@Param("dateFrom") String dateFrom,
                                               @Param("dateTo") String dateTo,
                                               @Param("organizationId") String organizationId,
                                               @Param("noticeNumbers") Set<String> noticeNumbers);

    @Query("SELECT DISTINCT VALUE c.operationId " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND (ARRAY_CONTAINS(@sessionIds, c.sessionId) " +
            "OR ((ARRAY_CONTAINS(@noticeNumbers, c.noticeNumber) AND c.domainId = @organizationId) OR ARRAY_CONTAINS(@paymentTokens, c.paymentToken))) " +
            "AND IS_DEFINED(c.operationId) AND c.operationId != null")
    Set<String> findOperationIdByFoundData(@Param("dateFrom") String dateFrom,
                                           @Param("dateTo") String dateTo,
                                           @Param("organizationId") String organizationId,
                                           @Param("sessionIds") Set<String> sessionIds,
                                           @Param("noticeNumbers") Set<String> noticeNumbers,
                                           @Param("paymentTokens") Set<String> paymentTokens);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND ARRAY_CONTAINS(@operationIds, c.operationId) " +
            "OR ARRAY_CONTAINS(@sessionIds, c.sessionId) " +
            "ORDER BY c._ts ASC")
    List<ReEventEntity> findByOperationIdAndSessionId(@Param("dateFrom") String dateFrom,
                                                      @Param("dateTo") String dateTo,
                                                      @Param("operationIds") Set<String> operationIds,
                                                      @Param("sessionIds") Set<String> sessionIds);
}