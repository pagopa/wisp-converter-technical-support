package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReEventExperimentalRepository extends CosmosRepository<ReEventEntity, String> {

    @Query("SELECT * " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.sessionId = @sessionId " +
            "AND c.eventCategory = 'INTERFACE' " +
            "ORDER BY c._ts ASC")
    Set<ReEventEntity> findOnlyMainStatuses(@Param("dateFrom") String dateFrom,
                                            @Param("dateTo") String dateTo,
                                            @Param("sessionId") String sessionId);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE (c.partitionKey >= @dateFrom AND c.partitionKey <= @dateTo) " +
            "AND c.sessionId = @sessionId " +
            "ORDER BY c._ts ASC")
    Set<ReEventEntity> findAllStatuses(@Param("dateFrom") String dateFrom,
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

}