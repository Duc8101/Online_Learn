package online_learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@SpringBootApplication
public class OnlineLearnApplication {

	public static void main(String[] args) throws IOException {
		URI uri = URI.create("http://localhost:8080/Home");
		Desktop.getDesktop().browse(uri);
		SpringApplication.run(OnlineLearnApplication.class, args);
	}

}
