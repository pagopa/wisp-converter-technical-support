package it.gov.pagopa.wispconverter.technicalsupport.repository.model.re;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReEventDataExplorerEntity {

    private String id;
    private Instant insertedTimestamp;
    private String operationId;
    private String tipoEvento;
    private String sottoTipoEvento;
    private String categoriaEvento;
    private String idDominio;
    private String iuv;
    private String ccp;
    private String psp;
    private String stazione;
    private String canale;
    private String payload;
    private String noticeNumber;
    private String paymentToken;
}
