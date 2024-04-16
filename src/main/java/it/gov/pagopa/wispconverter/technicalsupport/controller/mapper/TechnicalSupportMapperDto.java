package it.gov.pagopa.wispconverter.technicalsupport.controller.mapper;

import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TechnicalSupportMapperDto {

  @Mapping(source = "idDominio", target = "organizationId")
  ReEvent toReEvent(ReEventDto source);
  List<ReEvent> toReEventList(List<ReEventDto> reEventDtoList);

}
