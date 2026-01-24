package br.com.bytefood.email_notification.service.impl;

import br.com.bytefood.email_notification.dtos.NotificationDTO;
import br.com.bytefood.email_notification.entity.Notification;
import br.com.bytefood.email_notification.repository.NotificationRepository;
import br.com.bytefood.email_notification.service.NotificationService;
import br.com.bytefood.enums.NotificationType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("Inside sendEmail");
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom("joamarcosdev@gmail.com");
            helper.setTo(notificationDTO.getRecipient());
            helper.setSubject(notificationDTO.getSubject());
            helper.setText(notificationDTO.getBody(), notificationDTO.isHtml());

            javaMailSender.send(mimeMessage);


            Notification notificationToSave = Notification.builder()
                    .recipient(notificationDTO.getRecipient())
                    .subject(notificationDTO.getSubject())
                    .body(notificationDTO.getBody())
                    .type(NotificationType.EMAIL)
                    .isHtml(notificationDTO.isHtml())
                    .build();


            notificationRepository.save(notificationToSave);
            log.info("Saved to notification table ");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
