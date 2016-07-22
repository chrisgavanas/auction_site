package com.main;

import com.xmlparser.XmlParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories("com.webapplication.dao")
@ComponentScan({"com.webapplication", "com.xmlparser"})
@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);

        XmlParser xmlParser = app.getBean(XmlParser.class);
        xmlParser.parse();
    }
}
