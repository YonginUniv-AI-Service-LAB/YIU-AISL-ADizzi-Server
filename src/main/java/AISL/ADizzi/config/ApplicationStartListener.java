package AISL.ADizzi.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");
        System.out.println();
        System.out.println("URL: http://localhost:" + port);
        // 서버 배포 주소
        System.out.println("URL: http://52.78.164.15:" + port);
        System.out.println();
    }
}
