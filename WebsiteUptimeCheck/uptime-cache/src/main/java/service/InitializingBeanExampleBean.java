package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitializingBeanExampleBean implements InitializingBean {

    @Autowired
    WebsiteRegisterCache websiteRegisterCache;
    @Autowired
    WebsiteStatusCache websiteStatusCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Refreshing Cache from DB");
        websiteRegisterCache.initializeFromDB();
        websiteStatusCache.initializeFromDB();
    }
}