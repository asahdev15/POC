
package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class WebsiteStatus {
    private String name;
    private String url;
    private TypeWebsiteStatus websiteStatusType;
    private Date since;
    private long averageResponseTime;
    private int statusCount;
    private Date lastUpdate;
    private boolean isMonitoringActive;
}