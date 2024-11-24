package it.gov.pagopa.wispconverter.technicalsupport.repository;

import com.microsoft.azure.kusto.data.Client;
import com.microsoft.azure.kusto.data.KustoOperationResult;
import com.microsoft.azure.kusto.data.KustoResultSetTable;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.ReEventDataExplorerMapper;
import it.gov.pagopa.wispconverter.technicalsupport.exception.AppError;
import it.gov.pagopa.wispconverter.technicalsupport.exception.AppException;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventDataExplorerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReEventDataExplorerRepository {

    public static final String QUERY_FIND_BY_NOTICE_NUMBER_AND_CCP = """
            ReEvent
            | where insertedTimestamp >= todatetime("%sT00:00:00")
            | where insertedTimestamp <= todatetime("%sT23:59:59")
            | where sottoTipoEvento != 'INTERN'
            | where noticeNumber == '%s' and idDominio == '%s' and (ccp == '' or ccp == '%s')
            | order by insertedTimestamp""";

    public static final String QUERY_FIND_BY_NOTICE_NUMBER = """
            ReEvent
            | where insertedTimestamp >= todatetime("%sT00:00:00")
            | where insertedTimestamp <= todatetime("%sT23:59:59")
            | where sottoTipoEvento != 'INTERN'
            | where noticeNumber == '%s' and idDominio == '%s'
            | order by insertedTimestamp""";

    private final ReEventDataExplorerMapper reEventDataExplorerMapper;

    private final Client kustoClient;

    @Value("${azure.dataexplorer.dbName}")
    private String database;

    public List<ReEventDataExplorerEntity> find(String dateFrom, String dateTo, String noticeNumber, String domainId, String ccp) {

        List<ReEventDataExplorerEntity> result = new LinkedList<>();

        String query;
        if (ccp == null || ccp.isEmpty()) {
            query = String.format(QUERY_FIND_BY_NOTICE_NUMBER, dateFrom, dateTo, noticeNumber, domainId);
        } else {
            query = String.format(QUERY_FIND_BY_NOTICE_NUMBER_AND_CCP, dateFrom, dateTo, noticeNumber, domainId, ccp);
        }

        try {
            KustoOperationResult response = kustoClient.execute(database, query);
            KustoResultSetTable primaryResults = response.getPrimaryResults();
            while (primaryResults.hasNext()) {
                primaryResults.next();
                result.add(reEventDataExplorerMapper.toEntity(primaryResults));
            }
        } catch (Exception e) {
            throw new AppException(AppError.INTERNAL_SERVER_ERROR, e);
        }

        return result;
    }
}
