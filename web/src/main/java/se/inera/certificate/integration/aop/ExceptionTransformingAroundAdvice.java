package se.inera.certificate.integration.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import se.inera.certificate.integration.ResultOfCallUtil;
import se.inera.certificate.response.WebServiceResponse;

/**
 * AspectJ around advice applied around all web service methods.
 *
 * If the surrounded web service method call results in an exception, this advice handles the exception and converts it
 * to a proper web service response containing a ResultOfCall with result code set to ERROR. The exception's message
 * will be used as error text.
 *
 * @author andreaskaltenbach
 */
@Aspect
@Order(1)
public class ExceptionTransformingAroundAdvice {

    @Around("execution(public se.inera.certificate.response.WebServiceResponse+ se.inera.certificate.integration.*.*(..))")
    public Object doRecoveryActions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            if (joinPoint.getSignature() instanceof MethodSignature) {
                Class<WebServiceResponse> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                WebServiceResponse webServiceResponse = returnType.newInstance();
                webServiceResponse.setResult(ResultOfCallUtil.applicationErrorResult(throwable.getMessage()));
                return webServiceResponse;
            }
            throw throwable;
        }

    }
}
