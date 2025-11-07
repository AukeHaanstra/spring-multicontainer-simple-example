package nl.pancompany.multicontainer;

import jakarta.annotation.PreDestroy;
import nl.pancompany.othercontainer.OtherApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ContainerHolder {

    AnnotationConfigServletWebServerApplicationContext webContext;

    ContainerHolder(ConfigurableApplicationContext parentContext) {
        webContext = new AnnotationConfigServletWebServerApplicationContext();
        webContext.register(OtherApplication.class);
        webContext.setId("OtherContainerContext");
        webContext.getEnvironment().setActiveProfiles(parentContext.getEnvironment().getActiveProfiles());
        webContext.refresh();
    }

    @PreDestroy
    void stop() {
        if (webContext != null) {
            webContext.close();
        }
    }
}
