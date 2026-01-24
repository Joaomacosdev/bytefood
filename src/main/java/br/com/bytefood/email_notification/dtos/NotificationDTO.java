package br.com.bytefood.email_notification.dtos;

import br.com.bytefood.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {


    private Long id;
    private String subject;

    @NotBlank(message = "recipient is required")
    private String recipient;

    @Lob
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime createdAt;

    private boolean isHtml;

}
