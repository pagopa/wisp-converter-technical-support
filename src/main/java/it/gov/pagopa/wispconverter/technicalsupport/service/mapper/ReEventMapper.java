package it.gov.pagopa.wispconverter.technicalsupport.service.mapper;

import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReEventMapper {


    ReEventDto toReEventDto(ReEventEntity reEventEntity);
    List<ReEventDto> toReEventDtoList(List<ReEventEntity> reEventEntity);


}
