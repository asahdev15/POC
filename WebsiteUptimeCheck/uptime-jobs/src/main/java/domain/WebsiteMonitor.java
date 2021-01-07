package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ScheduledFuture;

@Data
@AllArgsConstructor
public class WebsiteMonitor {
    private WebsiteRegister websiteRegister;
    private ScheduledFuture<?> scheduledFuture;
}
