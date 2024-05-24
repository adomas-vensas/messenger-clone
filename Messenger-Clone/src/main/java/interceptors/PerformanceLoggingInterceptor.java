package interceptors;

import jakarta.interceptor.Interceptor;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import java.io.Serializable;

@Interceptor
@LogPerformance
public class PerformanceLoggingInterceptor implements Serializable {
    @AroundInvoke
    public Object logPerformance(InvocationContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            return context.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            String methodName = context.getMethod().getName();
            String className = context.getTarget().getClass().getName();
            System.out.printf("Method %s in class %s took %d milliseconds to execute.%n", methodName, className, executionTime);
        }
    }
}
