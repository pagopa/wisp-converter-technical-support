package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RPTRequestEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RPTRequestRepository extends CosmosRepository<RPTRequestEntity, String> {

    @Query("SELECT c.id, c.primitive " +
            "FROM c " +
            "WHERE c.PartitionKey = @date")
    List<RPTRequestEntity> findAllByPartitionKeyExcludingPayload(@Param("date") String date);
}