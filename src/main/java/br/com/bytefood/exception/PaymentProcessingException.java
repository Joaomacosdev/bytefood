package br.com.bytefood.exception;

public class PaymentProcessingException extends RuntimeException{

    public PaymentProcessingException(String message) {
        super(message);
    }
}
