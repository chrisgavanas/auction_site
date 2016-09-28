package com.webapplication.scheduler;


import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class CleanImages {

    private final static int SCHEDULED_TIME_HOURS = 1000 * 60 * 60 * 24;

    @Value("${imagesPath}")
    private String imagesPath;

    @Scheduled(fixedDelay = SCHEDULED_TIME_HOURS)
    private void cleanImages() throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(imagesPath));
        paths.forEach(filePath -> {
            if (Files.isRegularFile(filePath) && !filePath.toString().contains("FINALIZED_AUCTION_")) {
                try {
                    BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                    LocalDateTime creationTime = new LocalDateTime(attr.creationTime().toMillis());
                    if (creationTime.isBefore(LocalDateTime.now().minusHours(5)))
                        filePath.toFile().delete();
                } catch (IOException ignored) {
                }
            }
        });
    }

}
