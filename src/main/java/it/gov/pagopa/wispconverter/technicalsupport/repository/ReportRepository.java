package it.gov.pagopa.wispconverter.technicalsupport.repository;


import com.azure.spring.data.cosmos.repository.CosmosRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.report.ReportEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CosmosRepository<ReportEntity, String> {

}