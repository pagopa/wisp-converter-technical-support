package it.gov.pagopa.wispconverter.technicalsupport.controller.mapper;

import com.microsoft.azure.kusto.data.KustoResultSetTable;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.EventCategoryEnum;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventDataExplorerEntity;
import it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Base64;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReEventDataExplorerMapper {

    public ReEventDataExplorerEntity toEntity(KustoResultSetTable row) {
        return ReEventDataExplorerEntity.builder()
                .id(row.getString("uniqueId"))
                .insertedTimestamp(Instant.parse(row.getString("insertedTimestamp")))
                .operationId(row.getString("sessionId"))
                .categoriaEvento(row.getString("categoriaEvento"))
                .sottoTipoEvento(row.getString("sottoTipoEvento"))
                .idDominio(row.getString("idDominio"))
                .iuv(row.getString("iuv"))
                .ccp(row.getString("ccp"))
                .psp(row.getString("psp"))
                .tipoEvento(row.getString("tipoEvento"))
                .stazione(row.getString("stazione"))
                .canale(row.getString("canale"))
                .payload(row.getString("payload"))
                .noticeNumber(row.getString("noticeNumber"))
                .paymentToken(row.getString("paymentToken"))
                .build();
    }

    public ReEvent toReEvent(ReEventDataExplorerEntity ev1, ReEventDataExplorerEntity ev2) {
        ReEventDataExplorerEntity requestEvent;
        ReEventDataExplorerEntity responseEvent;
        if (ev2 == null || "REQ".equals(ev1.getSottoTipoEvento())) {
            requestEvent = ev1;
            responseEvent = ev2;
        } else {
            requestEvent = ev2;
            responseEvent = ev1;
        }

        String requestPayload = requestEvent.getPayload() != null ? new String(Base64.getDecoder().decode(requestEvent.getPayload())) : null;
        String responsePayload = responseEvent != null && responseEvent.getPayload() != null ? new String(Base64.getDecoder().decode(responseEvent.getPayload())) : null;


        Instant requestTimestamp = requestEvent.getInsertedTimestamp();
        return ReEvent.builder()
                .id(requestEvent.getId())
                .businessProcess(requestEvent.getTipoEvento())
                .operationId(requestEvent.getOperationId())
                .insertedTimestamp(requestTimestamp.atZone(java.time.ZoneId.of("Europe/Rome")).toInstant())
                .eventCategory(EventCategoryEnum.INTERFACE)
                .executionTimeMs(responseEvent != null ? responseEvent.getInsertedTimestamp().toEpochMilli() - requestTimestamp.toEpochMilli() : -1)
                .status("NDP_STEP_PROCESSED")
                .requestPayload(CommonUtility.nullify(requestPayload))
                .responsePayload(CommonUtility.nullify(responsePayload))
                .iuv(CommonUtility.nullify(requestEvent.getIuv()))
                .noticeNumber(CommonUtility.nullify(requestEvent.getNoticeNumber()))
                .ccp(CommonUtility.nullify(requestEvent.getCcp()))
                .domainId(CommonUtility.nullify(requestEvent.getIdDominio()))
                .station(CommonUtility.nullify(requestEvent.getStazione()))
                .psp(CommonUtility.nullify(requestEvent.getPsp()))
                .channel(CommonUtility.nullify(requestEvent.getCanale()))
                .paymentToken(CommonUtility.nullify(requestEvent.getPaymentToken()))
                .build();
    }
}
