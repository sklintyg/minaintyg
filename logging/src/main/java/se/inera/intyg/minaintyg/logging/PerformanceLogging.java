package se.inera.intyg.minaintyg.logging;

import static se.inera.intyg.minaintyg.logging.MdcLogConstants.EVENT_CATEGORY_API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceLogging {

  String eventType();

  String eventAction();

  String eventCategory() default EVENT_CATEGORY_API;

  boolean isActive() default true;

}
