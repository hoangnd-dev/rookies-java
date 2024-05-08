package nashtech.rookies.jpa.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class ApplicationBoot {

    private ApplicationBoot () {
    }

    public static <T> T with (Class<T> appClass) {
        var context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("jpa");
        context.register(AppConfig.class);
        context.register(appClass);
        context.refresh();
        context.registerShutdownHook();
        return context.getBean(appClass);

    }
}
