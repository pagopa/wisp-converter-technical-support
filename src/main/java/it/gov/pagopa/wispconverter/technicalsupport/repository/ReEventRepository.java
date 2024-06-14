package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReEventRepository extends CosmosRepository<ReEventEntity, String> {

    @Query("select * from c where c.partitionKey >= @dateFrom and c.partitionKey <= @dateTo and c.domainId = @organizationId and c.noticeNumber = @noticeNumber")
    List<ReEventEntity> findByIdDominioAndNoticeNumber(@Param("dateFrom") String dateFrom,
                                                       @Param("dateTo") String dateTo,
                                                       @Param("organizationId") String organizationId,
                                                       @Param("noticeNumber") String noticeNumber);

    @Query("select * from c where c.partitionKey >= @dateFrom and c.partitionKey <= @dateTo and c.domainId = @organizationId and c.iuv = @iuv")
    List<ReEventEntity> findByIdDominioAndIuv(@Param("dateFrom") String dateFrom,
                                              @Param("dateTo") String dateTo,
                                              @Param("organizationId") String organizationId,
                                              @Param("iuv") String iuv);

    @Query("select * from c where c.partitionKey >= @dateFrom and c.partitionKey <= @dateTo and c.operationId = @operationId")
    List<ReEventEntity> findByOperationId(@Param("dateFrom") String dateFrom,
                                          @Param("dateTo") String dateTo,
                                          @Param("operationId") String operationId);

}