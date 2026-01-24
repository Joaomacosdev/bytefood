package br.com.bytefood;

import br.com.bytefood.email_notification.dtos.NotificationDTO;
import br.com.bytefood.email_notification.service.NotificationService;
import br.com.bytefood.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class BytefoodApplication {

	private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(BytefoodApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(){
//		return args -> {
//			NotificationDTO notificationDTO = NotificationDTO.builder()
//					.recipient("yasmimaragao45@gmail.com")
//					.subject("Hello yasmim")
//					.body("Hey this is a test email")
//					.type(NotificationType.EMAIL)
//					.build();
//
//			notificationService.sendEmail(notificationDTO);
//		};
//	}
}
