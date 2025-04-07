package nashtech.rookies.jpa.config;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.Transactional;

//@Interceptor
public class TransactionHandler {

    @AroundInvoke
    public Object logMethodInvocation (InvocationContext context) throws Exception {
        var method = context.getMethod();
        if ( method.isAnnotationPresent(Transactional.class) ) {

            Object result = context.proceed();
            return result;
        }
        Object result = context.proceed();
        return result;
    }
}