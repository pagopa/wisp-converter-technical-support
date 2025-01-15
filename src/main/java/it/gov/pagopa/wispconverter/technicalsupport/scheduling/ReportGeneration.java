package it.gov.pagopa.wispconverter.technicalsupport.scheduling;

import it.gov.pagopa.wispconverter.technicalsupport.service.ReportGenerationService;
import it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@ConditionalOnProperty(name = "cron.job.schedule.report-generation.enabled", matchIfMissing = false)
public class ReportGeneration {

    private final ReportGenerationService reportGenerationService;

    @Autowired
    public ReportGeneration(ReportGenerationService reportGenerationService) {
        this.reportGenerationService = reportGenerationService;
    }


    @Scheduled(cron = "${cron.job.schedule.report-generation.daily.cron}")
    public void generateDailyReport() {

        log.info("[Report Generation][Trigg] Triggered cron invocation for daily generation.");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dayForReport = CommonUtility.partitionKeyFromInstant(yesterday);
        this.reportGenerationService.generateDailyReport(dayForReport);
    }

    @Scheduled(cron = "${cron.job.schedule.report-generation.weekly.cron}")
    public void generateWeeklyReport() {

        log.info("[Report Generation][Trigg] Triggered cron invocation for weekly generation.");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dayForReport = CommonUtility.partitionKeyFromInstant(yesterday);
        this.reportGenerationService.generateWeeklyReport(dayForReport);
    }

    @Scheduled(cron = "${cron.job.schedule.report-generation.monthly.cron}")
    public void generateMonthlyReport() {

        log.info("[Report Generation][Trigg] Triggered cron invocation for monthly generation.");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dayForReport = CommonUtility.partitionKeyFromInstant(yesterday);
        this.reportGenerationService.generateMonthlyReport(dayForReport);
    }
}
