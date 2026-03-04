package framework.core.listener;

import framework.core.annotation.Service;
import org.testng.IMethodInterceptor;
import org.testng.IMethodInstance;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceExecutionListener implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(
            List<IMethodInstance> methods,
            ITestContext context) {

        String serviceParam = System.getProperty("service");

        if (serviceParam == null || serviceParam.equalsIgnoreCase("all")) {
            return methods;
        }

        Set<String> requestedServices =
                Arrays.stream(serviceParam.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());

        List<IMethodInstance> filtered = new ArrayList<>();

        for (IMethodInstance methodInstance : methods) {

            Method method =
                    methodInstance.getMethod()
                            .getConstructorOrMethod()
                            .getMethod();

            Service annotation = method.getAnnotation(Service.class);

            if (annotation == null) {
                continue;
            }

            String testService =
                    annotation.value().toLowerCase();

            if (requestedServices.contains(testService)) {
                filtered.add(methodInstance);
            }
        }

        return filtered;
    }
}