package nl.pancompany.othercontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ContextAnnouncer {

    private static final Logger log = LoggerFactory.getLogger(ContextAnnouncer.class);

    private volatile Integer localPort; // set if/when a web server starts

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        printInfo((ConfigurableApplicationContext) event.getApplicationContext(), "REFRESHED");
    }

    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        this.localPort = event.getWebServer().getPort();
        printInfo((ConfigurableApplicationContext) event.getApplicationContext(), "WEB-SERVER-READY");
    }

    private void printInfo(ConfigurableApplicationContext ctx, String phase) {
        Environment env = ctx.getEnvironment();
        String appName = valueOr(env.getProperty("spring.application.name"), ctx.getApplicationName(), "(unknown)");
        String id = ctx.getId();
        String displayName = ctx.getDisplayName();
        String[] profiles = env.getActiveProfiles();
        int beanCount = ctx.getBeanDefinitionCount();
        String parentId = (ctx.getParent() != null) ? ctx.getParent().getId() : "(none)";
        String webType = webType(ctx);
        String port = (localPort != null) ? String.valueOf(localPort) : "(n/a)";

        String banner = """
                ------------------------------------------------------------
                Context %-16s | phase: %-16s
                name:      %s
                id:        %s
                display:   %s
                parent:    %s
                profiles:  %s
                beans:     %d
                web-type:  %s
                port:      %s
                ------------------------------------------------------------
                """.formatted(shorten(appName, 16), phase, appName, id, displayName,
                parentId, Arrays.toString(profiles), beanCount, webType, port);

        // Print to console and log—useful in both Boot and non-Boot starts
        System.out.println(banner);
        log.info("\n{}", banner);
    }

    private static String webType(ApplicationContext ctx) {
        if (ctx instanceof ServletWebServerApplicationContext) return "SERVLET";
        if (ctx instanceof ReactiveWebServerApplicationContext) return "REACTIVE";
        return "NONE";
    }

    private static String valueOr(String... candidates) {
        for (String c : candidates) if (c != null && !c.isBlank()) return c;
        return "";
    }

    private static String shorten(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}