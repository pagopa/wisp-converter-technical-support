package it.gov.pagopa.wispconverter.technicalsupport.service.mapper;

import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import it.gov.pagopa.wispconverter.technicalsupport.util.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReEventMapper {

    @Mapping(target = "httpHeaders", expression = "java(obfuscateSecrets(reEventEntity.getHttpHeaders()))")
    public abstract ReEventDto toReEventDto(ReEventEntity reEventEntity);

    public abstract List<ReEventDto> toReEventDtoList(List<ReEventEntity> reEventEntity);

    public String obfuscateSecrets(String httpHeaders) {
        if (httpHeaders != null) {
            httpHeaders = httpHeaders.replaceAll(Constants.SUBSCRIPTION_KEY_PATTERN, "\"Ocp-Apim-Subscription-Key\": [***]");
        }
        return httpHeaders;
    }
}
