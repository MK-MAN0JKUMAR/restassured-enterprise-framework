package framework.core.service;

import framework.core.annotation.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class ServiceRegistry {

    private ServiceRegistry(){}

    public static Set<String> discoverServices() {

        Set<String> services = new HashSet<>();

        try {

            String basePath =
                    new File("target/test-classes").getAbsolutePath();

            scanDirectory(new File(basePath), "", services);

        } catch (Exception e) {
            throw new RuntimeException("Service discovery failed", e);
        }

        return services;
    }

    private static void scanDirectory(File dir,
                                      String packageName,
                                      Set<String> services) {

        if (!dir.exists()) return;

        for(File file : dir.listFiles()) {

            if(file.isDirectory()) {

                scanDirectory(
                        file,
                        packageName + file.getName() + ".",
                        services
                );

            } else if(file.getName().endsWith(".class")) {

                String className =
                        packageName + file.getName().replace(".class","");

                try {

                    Class<?> clazz = Class.forName(className);

                    for(Method method : clazz.getDeclaredMethods()) {

                        Service annotation =
                                method.getAnnotation(Service.class);

                        if(annotation != null) {
                            services.add(annotation.value());
                        }

                    }

                } catch (Throwable ignored){}

            }
        }
    }
}