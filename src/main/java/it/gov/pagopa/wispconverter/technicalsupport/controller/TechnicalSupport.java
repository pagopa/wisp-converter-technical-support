package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.TechnicalSupportMapperDto;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.PayloadResponse;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.service.PayloadDto;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReEventDto;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static it.gov.pagopa.wispconverter.technicalsupport.util.Constants.*;

@RestController
@Validated
@RequestMapping("/api/ts/v1/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Technical Support", description = "API for read event and payload from table storage")
public class TechnicalSupport {

    private final ReService reService;
    private final TechnicalSupportMapperDto technicalSupportMapperDto;

    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved event",
            content = @Content( schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/organizations/{"+ORGANIZATION+"}/notice-number/{"+NOTICE_NUMBER+"}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findByNoticeNumber(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Id dell'EC") String organizationId,
            @PathVariable(name = NOTICE_NUMBER) @Schema(example = "3982273480765907", description = "Numero avviso") String noticeNumber,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Data di ricerca DA") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Data di ricerca A") LocalDate dateTo) {

        List<ReEventDto> byNoticeNumber = reService.findByNoticeNumber(dateFrom, dateTo, organizationId, noticeNumber);
        List<ReEvent> reEventList = technicalSupportMapperDto.toReEventList(byNoticeNumber);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved event",
            content = @Content( schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/organizations/{"+ORGANIZATION+"}/iuv/{"+IUV+"}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Id dell'EC") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "982273480765907", description = "Identificativo univoco del versamento") String iuv,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Data di ricerca DA") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Data di ricerca A") LocalDate dateTo) {
        List<ReEventDto> byNoticeNumber = reService.findByIuv(dateFrom, dateTo, organizationId, iuv);
        List<ReEvent> reEventList = technicalSupportMapperDto.toReEventList(byNoticeNumber);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved event",
            content = @Content( schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/session-id/{"+SESSION_ID+"}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findBySessionId(
            @PathVariable(name = SESSION_ID) @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione eseguita") String sessionId,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Data di ricerca DA") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Data di ricerca A") LocalDate dateTo) {
        List<ReEventDto> byNoticeNumber = reService.findBySessionId(dateFrom, dateTo, sessionId);
        List<ReEvent> reEventList = technicalSupportMapperDto.toReEventList(byNoticeNumber);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved gzip payload",
                    content = @Content( schema = @Schema(implementation = PayloadResponse.class)))
    })
    @GetMapping(value = "/payload-ref-id/{"+PAYLOAD_REF_ID+"}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayloadResponse getPayloadByPayloadRefId(
            @PathVariable(name = PAYLOAD_REF_ID) @Schema(example = "2024-03-18_-230539074458600920", description = "Nome del file") String payloadRefId) {
        PayloadDto payloadDto = reService.fetchPayload(payloadRefId);
        return technicalSupportMapperDto.toPayloadResponse(payloadDto);
    }
}
