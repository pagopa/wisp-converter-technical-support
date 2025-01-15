package it.gov.pagopa.wispconverter.technicalsupport.controller.model.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfo {

    private String name;
    private String version;
    private String environment;
}
