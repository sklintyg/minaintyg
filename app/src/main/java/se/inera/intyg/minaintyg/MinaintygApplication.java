package se.inera.intyg.minaintyg;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.inera.intyg.minaintyg.config.LockProviderConfig;

@EnableScheduling
@SpringBootApplication
@EnableSchedulerLock(defaultLockAtMostFor = LockProviderConfig.DEFAULT_LOCK_AT_MOST)
public class MinaintygApplication {

  public static void main(String[] args) {
    SpringApplication.run(MinaintygApplication.class, args);
  }
}
