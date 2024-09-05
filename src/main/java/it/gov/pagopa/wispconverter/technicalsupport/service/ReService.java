package it.gov.pagopa.wispconverter.technicalsupport.service;

import it.gov.pagopa.wispconverter.technicalsupport.exception.AppError;
import it.gov.pagopa.wispconverter.technicalsupport.exception.AppException;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.mapper.ReEventMapper;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReService {

    static final String PATTERN_FORMAT = "yyyy-MM-dd";

    private final ReEventRepository reEventRepository;
    private final ReEventMapper reEventMapper;

    public static String partitionKeyFromInstant(LocalDate insertedTimestamp) {
        return insertedTimestamp == null ? null : DateTimeFormatter
                .ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault())
                .format(insertedTimestamp);
    }

    public List<ReEventDto> findByNoticeNumber(LocalDate dateFromAsLocalDate, LocalDate dateToAsLocalDate, String organization, String noticeNumber) {

        String dateFrom = partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByNoticeNumberAndDomainId(dateFrom, dateTo, organization, noticeNumber);
        Set<String> noticeNumbers = reEventRepository.findNoticeNumberBySessionId(dateFrom, dateTo, sessionIds);
        Set<String> paymentTokens = reEventRepository.findPaymentTokenByNoticeNumber(dateFrom, dateTo, organization, noticeNumbers);
        Set<String> operationIds = reEventRepository.findOperationIdByFoundData(dateFrom, dateTo, organization, sessionIds, noticeNumbers, paymentTokens);
        List<ReEventEntity> reEventEntities = reEventRepository.findByOperationIdAndSessionId(dateFrom, dateTo, operationIds, sessionIds);

        return reEventMapper.toReEventDtoList(reEventEntities);
    }

    public List<ReEventDto> findByIuv(LocalDate dateFromAsLocalDate, LocalDate dateToAsLocalDate, String organization, String iuv) {

        String dateFrom = partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByIuvAndDomainId(dateFrom, dateTo, organization, iuv);
        Set<String> noticeNumbers = reEventRepository.findNoticeNumberBySessionId(dateFrom, dateTo, sessionIds);
        Set<String> paymentTokens = reEventRepository.findPaymentTokenByNoticeNumber(dateFrom, dateTo, organization, noticeNumbers);
        Set<String> operationIds = reEventRepository.findOperationIdByFoundData(dateFrom, dateTo, organization, sessionIds, noticeNumbers, paymentTokens);
        List<ReEventEntity> reEventEntities = reEventRepository.findByOperationIdAndSessionId(dateFrom, dateTo, operationIds, sessionIds);

        return reEventMapper.toReEventDtoList(reEventEntities);
    }

    public List<ReEventDto> findBySessionId(LocalDate dateFromAsLocalDate, LocalDate dateToAsLocalDate, String sessionId) {

        String dateFrom = partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = Set.of(sessionId);
        List<String> organizations = reEventRepository.findDomainIdBySessionId(dateFrom, dateTo, sessionId);
        if (organizations.size() != 1) {
            throw new AppException(AppError.INVALID_SESSIONID);
        }

        String organization = organizations.get(0);
        Set<String> noticeNumbers = reEventRepository.findNoticeNumberBySessionId(dateFrom, dateTo, sessionIds);
        Set<String> paymentTokens = reEventRepository.findPaymentTokenByNoticeNumber(dateFrom, dateTo, organization, noticeNumbers);
        Set<String> operationIds = reEventRepository.findOperationIdByFoundData(dateFrom, dateTo, organization, sessionIds, noticeNumbers, paymentTokens);
        List<ReEventEntity> reEventEntities = reEventRepository.findByOperationIdAndSessionId(dateFrom, dateTo, operationIds, sessionIds);

        return reEventMapper.toReEventDtoList(reEventEntities);
    }

    public List<ReEventDto> findByOperationId(LocalDate datefrom, LocalDate dateTo, String operationId) {
        List<ReEventEntity> reEventEntities = reEventRepository.findByOperationId(
                partitionKeyFromInstant(datefrom),
                partitionKeyFromInstant(dateTo),
                operationId);
        return reEventMapper.toReEventDtoList(reEventEntities);
    }

}
