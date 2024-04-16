package it.gov.pagopa.wispconverter.technicalsupport.service;

import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.mapper.ReEventMapper;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.ReEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReService {

    static final String PATTERN_FORMAT = "yyyy-MM-dd";
    
    private final ReEventRepository reEventRepository;
    private final ReEventMapper reEventMapper;

    public List<ReEventDto> findByNoticeNumber(LocalDate datefrom, LocalDate dateTo, String organization, String noticeNumber){
        List<ReEventEntity> reEventEntities = reEventRepository.findByIdDominioAndNoticeNumber(
                partitionKeyFromInstant(datefrom),
                partitionKeyFromInstant(dateTo),
                organization,
                noticeNumber);
        return reEventMapper.toReEventDtoList(reEventEntities);
    }

    public List<ReEventDto> findByIuv(LocalDate datefrom, LocalDate dateTo, String organization, String iuv){
        List<ReEventEntity> reEventEntities = reEventRepository.findByIdDominioAndIuv(
                partitionKeyFromInstant(datefrom),
                partitionKeyFromInstant(dateTo),
                organization,
                iuv);
        return reEventMapper.toReEventDtoList(reEventEntities);
    }

    public List<ReEventDto> findByOperationId(LocalDate datefrom, LocalDate dateTo, String operationId){
        List<ReEventEntity> reEventEntities = reEventRepository.findByOperationId(
                partitionKeyFromInstant(datefrom),
                partitionKeyFromInstant(dateTo),
                operationId);
        return reEventMapper.toReEventDtoList(reEventEntities);
    }


    public static String partitionKeyFromInstant(LocalDate insertedTimestamp) {
        return insertedTimestamp == null ? null : DateTimeFormatter
                .ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault())
                .format(insertedTimestamp);
    }

}
