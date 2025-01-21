package se.inera.intyg.minaintyg.logging;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class PerformanceLoggingAdvice {

  @Around("@annotation(performanceLogging)")
  public Object logPerformance(ProceedingJoinPoint joinPoint, PerformanceLogging performanceLogging)
      throws Throwable {
    if (performanceLogging.isActive()) {
      final var start = LocalDateTime.now();
      var success = true;
      try {
        return joinPoint.proceed();
      } catch (final Throwable throwable) {
        success = false;
        throw throwable;
      } finally {
        final var end = LocalDateTime.now();
        final var duration = Duration.between(start, end).toMillis();
        final var className = joinPoint.getSignature().getDeclaringTypeName();
        final var methodName = joinPoint.getSignature().getName();
        try (final var mdcLogConstants =
            MdcCloseableMap.builder()
                .put(MdcLogConstants.EVENT_START, start.toString())
                .put(MdcLogConstants.EVENT_END, end.toString())
                .put(MdcLogConstants.EVENT_DURATION, Long.toString(duration))
                .put(MdcLogConstants.EVENT_ACTION, performanceLogging.eventAction())
                .put(MdcLogConstants.EVENT_TYPE, performanceLogging.eventType())
                .put(MdcLogConstants.EVENT_CATEGORY, performanceLogging.eventCategory())
                .put(MdcLogConstants.EVENT_CLASS, className)
                .put(MdcLogConstants.EVENT_METHOD, methodName)
                .put(MdcLogConstants.EVENT_OUTCOME, success ? "success" : "failure")
                .build()
        ) {
          log.info(LogMarkers.PERFORMANCE, "Class: {} Method: {} Duration: {} ms",
              className,
              methodName,
              duration
          );
        }
      }
    }
    return joinPoint.proceed();
  }
}
