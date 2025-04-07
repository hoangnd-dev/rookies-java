package nashtech.rookies.jpa.config;

import jakarta.enterprise.inject.se.SeContainerInitializer;

public class CDIBoot {

    public static <T> T with (Class<T> appClass) {

        var instance = SeContainerInitializer.newInstance();
        try (var container = instance.addBeanClasses(appClass, AppConfig.class).initialize()) {
            return container.select(appClass).get();
        }
    }
}
