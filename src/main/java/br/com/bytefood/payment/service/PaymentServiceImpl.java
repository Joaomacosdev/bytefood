package br.com.bytefood.payment.service;

import br.com.bytefood.email_notification.dtos.NotificationDTO;
import br.com.bytefood.email_notification.service.NotificationService;
import br.com.bytefood.enums.OrderStatus;
import br.com.bytefood.enums.PaymentGateway;
import br.com.bytefood.enums.PaymentStatus;
import br.com.bytefood.exception.BadRequestException;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.order.entity.Order;
import br.com.bytefood.order.repository.OrderRepository;
import br.com.bytefood.payment.dtos.PaymentDTO;
import br.com.bytefood.payment.entity.Payment;
import br.com.bytefood.payment.repository.PaymentRepository;
import br.com.bytefood.response.Response;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final TemplateEngine templateEngine;
    private final ModelMapper modelMapper;

    @Value("${stripe.api.secret.key}")
    private String secretKey;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @Override
    public Response<?> initializePayment(PaymentDTO paymentRequest) {
        log.info("Inside initializePayment()");

        Stripe.apiKey = secretKey;

        Long orderId = paymentRequest.getOrderId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order Not found"));

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED){
            throw new BadRequestException("Payment Already Made for this order");
        }

        if (order.getTotalAmount() == null ){
            throw new BadRequestException("Amount you are passing in is null");
        }

        if (order.getTotalAmount().compareTo(paymentRequest.getAmount()) != 0 ){
            throw new BadRequestException("Payment amount does not tally, Please Contact out customer support agent");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency("usd")
                    .putMetadata("orderId", String.valueOf(orderId))
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            String uniqueTransactionalId = intent.getClientSecret();

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("success")
                    .data(uniqueTransactionalId)
                    .build();

        }catch (Exception e){
            throw new RuntimeException("Error Creating payment unique transaction id");
        }

    }

    @Override
    public void updatePaymentForOrder(PaymentDTO paymentDTO) {

        log.info("Inside updatePaymentForOrder()");

        Long orderId = paymentDTO.getOrderId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order Not Found"));

        Payment payment = new Payment();
        payment.setPaymentGateway(PaymentGateway.STRIPE);
        payment.setAmount(paymentDTO.getAmount());
        payment.setTransactionId(payment.getTransactionId());
        payment.setPaymentStatus(paymentDTO.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setOrder(order);

        if (!paymentDTO.isSuccess()){
            payment.setFailureReasons(paymentDTO.getFailureReason());
        }

        paymentRepository.save(payment);

        Context context = new Context(Locale.getDefault());
        context.setVariable("customerName", order.getUser().getName());
        context.setVariable("orderId", order.getId());
        context.setVariable("currentYear", Year.now().getValue());
        context.setVariable("amount", "$" + paymentDTO.getAmount());

        if (paymentDTO.isSuccess()) {
            order.setPaymentStatus(PaymentStatus.COMPLETED);
            order.setOrderStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);


            log.info("PAYMENT IS SUCCESSFUL ABOUT TO SEND EMAIL");

            // Add success-specific variables
            context.setVariable("transactionId", paymentDTO.getTransactionId());
            context.setVariable("paymentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));
            context.setVariable("frontendBaseUrl", this.frontendBaseUrl);

            String emailBody = templateEngine.process("payment-success", context);

            log.info("HAVE GOTTEN TEMPLATE");

            notificationService.sendEmail(NotificationDTO.builder()
                    .recipient(order.getUser().getEmail())
                    .subject("Payment Successful - Order #" + order.getId())
                    .body(emailBody)
                    .isHtml(true)
                    .build());
        } else {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);


            log.info("PAYMENT IS FAILED ABOUT TO SEND EMAIL");
            // Add failure-specific variables
            context.setVariable("failureReason", paymentDTO.getFailureReason());

            String emailBody = templateEngine.process("payment-failed", context);

            notificationService.sendEmail(NotificationDTO.builder()
                    .recipient(order.getUser().getEmail())
                    .subject("Payment Failed - Order #" + order.getId())
                    .body(emailBody)
                    .isHtml(true)
                    .build());
        }

    }

    @Override
    public Response<List<PaymentDTO>> getAllPayments() {
        log.info("inside getAllPayments()");

        List<Payment> paymentList = paymentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<PaymentDTO> paymentDTOS = modelMapper.map(paymentList, new TypeToken<List<PaymentDTO>>() {}.getType());

        paymentDTOS.forEach(item -> {
            item.setOrder(null);
            item.setUser(null);
        });

        return Response.<List<PaymentDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("payment retreived succeessfully")
                .data(paymentDTOS)
                .build();

    }

    @Override
    public Response<PaymentDTO> getPaymentById(Long paymentId) {
        log.info("inside getPaymentById()");

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()-> new NotFoundException("Payment not found"));
        PaymentDTO paymentDTOS = modelMapper.map(payment, PaymentDTO.class);

        paymentDTOS.getUser().setRoles(null);
        paymentDTOS.getOrder().setUser(null);
        paymentDTOS.getOrder().getOrderItems().forEach(item->{
            item.getMenu().setReviews(null);
        });

        return Response.<PaymentDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("payment retreived succeessfully by id")
                .data(paymentDTOS)
                .build();
    }
}
