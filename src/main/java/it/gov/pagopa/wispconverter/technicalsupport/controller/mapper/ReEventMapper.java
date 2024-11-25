package it.gov.pagopa.wispconverter.technicalsupport.controller.mapper;

import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.util.Constants;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReEventMapper {

    @Named("toDto")
    @Mapping(target = "requestHeaders", expression = "java(obfuscateSecrets(reEventEntity.getRequestHeaders()))")
    @Mapping(target = "responseHeaders", expression = "java(obfuscateSecrets(reEventEntity.getResponseHeaders()))")
    @Mapping(target = "requestPayload", expression = "java(it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility.decompressGZip(reEventEntity.getRequestPayload()))")
    @Mapping(target = "responsePayload", expression = "java(it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility.decompressGZip(reEventEntity.getResponsePayload()))")
    @Mapping(target = "operationId", expression = "java(reEventEntity.getOperationId() != null ? reEventEntity.getOperationId() : reEventEntity.getSessionId())")
    @Mapping(target = "insertedTimestamp", expression = "java(reEventEntity.getInsertedTimestamp().plus(1, java.time.temporal.ChronoUnit.HOURS))")
    public abstract ReEvent toDto(ReEventEntity reEventEntity);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<ReEvent> toDto(List<ReEventEntity> reEventEntity);

    public void compact(ReEvent event) {
        event.setId(null);
        event.setOperationId(null);
        event.setHttpMethod(null);
        event.setExecutionTimeMs(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setOperationErrorLine(null);
        event.setOperationErrorDetail(null);
        event.setSessionId(null);
        event.setPsp(null);
        event.setChannel(null);
        event.setInfo(null);
        event.setRequestPayload(null);
        event.setResponsePayload(null);
    }

    public String obfuscateSecrets(String httpHeaders) {
        if (httpHeaders != null) {
            httpHeaders = httpHeaders.replaceAll(Constants.SUBSCRIPTION_KEY_PATTERN, "\"Ocp-Apim-Subscription-Key\": [***]");
        }
        return httpHeaders;
    }
}
