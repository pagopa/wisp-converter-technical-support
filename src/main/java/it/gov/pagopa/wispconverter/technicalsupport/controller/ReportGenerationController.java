package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReportGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@Validated
@RequiredArgsConstructor
@Tag(name = "Report Generation", description = "API for execute report generation on demand")
public class ReportGenerationController {

    private final ReportGenerationService reportGenerationService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @PostMapping(value = "/daily", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateDailyReport(
            @RequestParam(name = "day") @Schema(example = "2024-01-01", description = "The day on which the report will be generated (in yyyy-MM-dd)") String date) {

        reportGenerationService.generateDailyReport(date);
        return ResponseEntity.ok("Report generation completed!");
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @PostMapping(value = "/weekly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateWeeklyReport(
            @RequestParam(name = "day") @Schema(example = "2024-01-01", description = "The day on which it is calculated the previous day and from the weekly report will be generated (in yyyy-MM-dd)") String date) {

        reportGenerationService.generateWeeklyReport(date);
        return ResponseEntity.ok("Report generation completed!");
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(schema = @Schema(implementation = ReEventResponse.class)))
    })
    @PostMapping(value = "/monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateMonthlyReport(
            @RequestParam(name = "day") @Schema(example = "2024-01-01", description = "The day on which it is calculated the previous day and from the monthly report will be generated (in yyyy-MM-dd)") String date) {

        reportGenerationService.generateMonthlyReport(date);
        return ResponseEntity.ok("Report generation completed!");
    }
}
