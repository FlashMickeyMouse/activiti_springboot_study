package xin.kingsman.activiti;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Log4j2
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application activiti_springboot_study is running! Access URLs:\n\t" +
                "Local: \t\t\thttp://localhost:" + port + "/\n\t" +
                "External: \t\thttp://" + ip + ":" + port + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + "/swagger-ui.html\n\t" +
                "Doc: \t\t\thttp://" + ip + ":" + port + "/doc.html\n" +
                "Activiti: \t\t\thttp://" + ip + ":" + port + "/activiti/create\n" +
                "----------------------------------------------------------");
    }
}
