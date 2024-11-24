package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RTRepository extends CosmosRepository<RTEntity, String> {
}