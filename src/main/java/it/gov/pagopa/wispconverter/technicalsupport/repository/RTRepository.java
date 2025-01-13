package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTGroupedByStatusEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RTRepository extends CosmosRepository<RTEntity, String> {

    @Query("SELECT c.id, c.receiptStatus, c.receiptType " +
            "FROM c " +
            "WHERE c.sessionId = @sessionId")
    List<RTEntity> findStatusInfoBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT COUNT(1) AS eventStatusCount, c.receiptStatus " +
            "FROM c " +
            "WHERE c._ts*1000 >= DateTimeToTimestamp(@dateFrom) AND c._ts*1000 < DateTimeToTimestamp(@dateTo) " +
            "GROUP BY c.receiptStatus")
    Set<RTGroupedByStatusEntity> findByTimestampGroupByStatus(@Param("dateFrom") String dateFrom,
                                                              @Param("dateTo") String dateTo);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId")
    List<RTEntity> findAllStatus(@Param("dateFrom") String dateFrom,
                                 @Param("dateTo") String dateTo,
                                 @Param("domainId") String domainId);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId " +
            "AND c.receiptStatus != 'SENT'")
    List<RTEntity> findAllStatusExcludingSent(@Param("dateFrom") String dateFrom,
                                              @Param("dateTo") String dateTo,
                                              @Param("domainId") String domainId);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId " +
            "AND c.iuv IN (@iuvs)")
    List<RTEntity> findStatusByIuv(@Param("dateFrom") String dateFrom,
                                   @Param("dateTo") String dateTo,
                                   @Param("domainId") String domainId,
                                   @Param("iuvs") Set<String> iuvs);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId " +
            "AND c.iuv IN (@iuvs) " +
            "AND c.receiptStatus != 'SENT'")
    List<RTEntity> findStatusByIuvExcludingSent(@Param("dateFrom") String dateFrom,
                                                @Param("dateTo") String dateTo,
                                                @Param("domainId") String domainId,
                                                @Param("iuvs") Set<String> iuvs);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId " +
            "AND c.rt = null")
    List<RTEntity> findAllInPendingStatus(@Param("dateFrom") String dateFrom,
                                          @Param("dateTo") String dateTo,
                                          @Param("domainId") String domainId);

    @Query("SELECT * " +
            "FROM c " +
            "WHERE c.rtTimestamp >= DateTimeToTimestamp(@dateFrom) AND c.rtTimestamp < DateTimeToTimestamp(@dateTo) " +
            "AND c.domainId = @domainId " +
            "AND c.iuv IN (@iuvs) " +
            "AND c.rt = null")
    List<RTEntity> findInPendingStatus(@Param("dateFrom") String dateFrom,
                                       @Param("dateTo") String dateTo,
                                       @Param("domainId") String domainId,
                                       @Param("iuvs") Set<String> iuvs);
}