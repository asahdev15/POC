import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import service.WebsiteRegisterCache;
import service.WebsiteStatusCache;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"rest", "service"})
public class RestApp {

   public static void main(String[] args) {
      SpringApplication.run(RestApp.class, args);
   }

   @PostConstruct
   public void appReady()
   {
      log.info("Cache Application Started");
   }

}
