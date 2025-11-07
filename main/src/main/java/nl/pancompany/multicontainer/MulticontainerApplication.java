package nl.pancompany.multicontainer;

import nl.pancompany.jpamodule.JpaModuleConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MulticontainerApplication {

	static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MulticontainerApplication.class, JpaModuleConfiguration.class)
                .web(WebApplicationType.NONE)
                .run(args);
	}

}
