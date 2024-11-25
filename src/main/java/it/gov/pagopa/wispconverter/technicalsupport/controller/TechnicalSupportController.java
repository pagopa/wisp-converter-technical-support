package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static it.gov.pagopa.wispconverter.technicalsupport.util.Constants.*;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "Technical Support", description = "API for read event and payload from table storage")
public class TechnicalSupportController {

    private final ReService reService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/notice-number/{" + NOTICE_NUMBER + "}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findByNoticeNumber(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Creditor institution identifier") String organizationId,
            @PathVariable(name = NOTICE_NUMBER) @Schema(example = "3982273480765907", description = "NAV payment code") String noticeNumber,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Lower limit date") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Upper limit date") LocalDate dateTo) {

        List<ReEvent> reEventList = reService.findByNoticeNumber(dateFrom, dateTo, organizationId, noticeNumber);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/iuv/{" + IUV + "}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Creditor institution identifier") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "982273480765907", description = "IUV payment code") String iuv,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Lower limit date") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Upper limit date") LocalDate dateTo) {

        List<ReEvent> reEventList = reService.findByIuv(dateFrom, dateTo, organizationId, iuv);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @GetMapping(value = "/session-id/{" + SESSION_ID + "}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventResponse findBySessionId(
            @PathVariable(name = SESSION_ID) @Schema(example = "12345678900_aa0518a9-194d-4de2-999f-e462c3746e24", description = "Session identifier of the flow") String sessionId,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-03-18", description = "Data di ricerca DA") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-03-18", description = "Data di ricerca A") LocalDate dateTo) {

        List<ReEvent> reEventList = reService.findBySessionId(dateFrom, dateTo, sessionId);
        return ReEventResponse.builder()
                .count(reEventList.size())
                .data(reEventList)
                .build();
    }
}
