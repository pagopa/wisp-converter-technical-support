package it.gov.pagopa.wispconverter.technicalsupport.service;

import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.ReEventMapper;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.re.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReService {

    private final ReEventRepository reEventRepository;

    private final ReEventMapper reEventMapper;

    public List<ReEvent> findByNoticeNumber(LocalDate dateFromAsLocalDate, LocalDate dateToAsLocalDate, String organization, String noticeNumber) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByNoticeNumberAndDomainId(dateFrom, dateTo, organization, noticeNumber);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, sessionIds);
    }

    public List<ReEvent> findByIuv(LocalDate dateFromAsLocalDate,
                                   LocalDate dateToAsLocalDate,
                                   String organization,
                                   String iuv) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByIuvAndDomainId(dateFrom, dateTo, organization, iuv);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, sessionIds);
    }

    public List<ReEvent> findBySessionId(LocalDate dateFromAsLocalDate, LocalDate dateToAsLocalDate, String sessionId) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, Set.of(sessionId));
    }

    private List<ReEvent> extractReEventsFromWispDismantling(String dateFrom, String dateTo, Set<String> sessionIds) {

        List<ReEventEntity> reEventEntities = new LinkedList<>();
        sessionIds.stream()
                .flatMap(sessionId -> reEventRepository.findBySessionId(dateFrom, dateTo, sessionId).stream())
                .forEach(reEventEntities::add);

        // convert all entities to DTOs then sort them by inserted timestamp
        List<ReEvent> reEvents = reEventMapper.toDto(reEventEntities);
        reEvents.sort(Comparator.comparing(ReEvent::getInsertedTimestamp));
        return reEvents;
    }
}
