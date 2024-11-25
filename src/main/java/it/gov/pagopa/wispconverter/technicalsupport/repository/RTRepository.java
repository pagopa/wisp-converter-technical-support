package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTGroupedByStatusEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RTRepository extends CosmosRepository<RTEntity, String> {


    @Query("SELECT COUNT(1) AS eventStatusCount, c.receiptStatus " +
            "FROM c " +
            "WHERE c._ts*1000 >= DateTimeToTimestamp(@dateFrom) AND c._ts*1000 < DateTimeToTimestamp(@dateTo) " +
            "GROUP BY c.receiptStatus")
    Set<RTGroupedByStatusEntity> findByTimestampGroupByStatus(@Param("dateFrom") String dateFrom,
                                                              @Param("dateTo") String dateTo);
}