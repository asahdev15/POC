package config;

import domain.WebsiteRegister;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import service.ScheduleTaskService;
import service.WebsiteRegisterService;
import service.WebsiteStatusService;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SpringConfig {

    @Autowired
    WebsiteRegisterService websiteRegisterService;
    @Autowired
    ScheduleTaskService scheduleTaskService;

    @Scheduled(fixedRate = 1000*60)
    public void scheduleFixedRateTask() {
        List<WebsiteRegister> registers = websiteRegisterService.fetchAll();
        log.info("Checking Monitoring Registers;size;{};",registers.size());
        registers.forEach(item -> scheduleTaskService.scheduleMonitoring(item));
    }

}