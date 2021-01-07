import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
      log.info("DB Application Started");
   }

}
