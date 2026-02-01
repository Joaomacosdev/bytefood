package br.com.bytefood.payment.service;

import br.com.bytefood.payment.dtos.PaymentDTO;
import br.com.bytefood.response.Response;

import java.util.List;

public interface PaymentService {

    Response<?> initializePayment(PaymentDTO paymentDTO);
    void updatePaymentForOrder(PaymentDTO paymentDTO);
    Response<List<PaymentDTO>> getAllPayments();
    Response<PaymentDTO> getPaymentById(Long paymentId);
}
