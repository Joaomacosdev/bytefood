package br.com.bytefood.email_notification.service;

import br.com.bytefood.email_notification.dtos.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO);
}
