package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.TechnicalSupportMapperDto;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReService;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static it.gov.pagopa.wispconverter.technicalsupport.util.Constants.*;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Technical Support", description = "API for read event and payload from table storage")
public class TechnicalSupportController {

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
    @GetMapping(value = "/operation-id/{"+OPERATION_ID+"}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findByOperationId(
            @PathVariable(name = OPERATION_ID) @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione eseguita") String operationId,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Data di ricerca DA") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Data di ricerca A") LocalDate dateTo) {
        List<ReEventDto> byNoticeNumber = reService.findByOperationId(dateFrom, dateTo, operationId);
        List<ReEvent> reEventList = technicalSupportMapperDto.toReEventList(byNoticeNumber);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

}
