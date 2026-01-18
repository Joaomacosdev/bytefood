package br.com.bytefood.menu.dtos;

import br.com.bytefood.review.dtos.ReviewDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDTO {

    private Long id;

    private String description;

    @NotNull(message = "Price is required")
    @Positive
    private BigDecimal price;

    @NotNull(message = "category ID is required")
    private Long categoryId;

    private MultipartFile imageFile;

    private List<ReviewDTO> reviews;

}
