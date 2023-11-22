package se.inera.intyg.minaintyg;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class MinaintygApplication {

  public static void main(String[] args) {
    SpringApplication.run(MinaintygApplication.class, args);
  }
}
