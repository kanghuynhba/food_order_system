package listener;

import entity.Payment;
import java.util.EventListener;

/**
 * PaymentListener - Payment Event Listener
 * Path: Source Packages/listener/PaymentListener.java
 * 
 * Interface for listening to payment events
 * Implement this interface to receive notifications when payments occur
 * 
 * @author Nguyễn Trương Quốc Huân & Huỳnh Bá Khang
 * @version 1.0
 */
public interface PaymentListener extends EventListener {
    
    /**
     * Called when a payment is initiated
     * 
     * @param payment The payment being initiated
     */
    void onPaymentInitiated(Payment payment);
    
    /**
     * Called when a payment is processing
     * 
     * @param payment The payment being processed
     * @param method Payment method (0: Cash, 1: Transfer, etc.)
     */
    void onPaymentProcessing(Payment payment, int method);
    
    /**
     * Called when a payment is successful
     * 
     * @param payment The successful payment
     */
    void onPaymentSuccess(Payment payment);
    
    /**
     * Called when a payment fails
     * 
     * @param payment The failed payment
     * @param reason Reason for failure
     */
    void onPaymentFailed(Payment payment, String reason);
    
    /**
     * Called when a payment is cancelled
     * 
     * @param payment The cancelled payment
     */
    void onPaymentCancelled(Payment payment);
    
    /**
     * Called when a payment is refunded
     * 
     * @param payment The refunded payment
     * @param refundAmount Amount refunded
     */
    void onPaymentRefunded(Payment payment, double refundAmount);
    
    /**
     * Called when payment method is changed
     * 
     * @param payment The payment
     * @param oldMethod Previous payment method
     * @param newMethod New payment method
     */
    void onPaymentMethodChanged(Payment payment, int oldMethod, int newMethod);
    
    /**
     * Called when a receipt is generated
     * 
     * @param payment The payment
     * @param receiptId ID of generated receipt
     */
    void onReceiptGenerated(Payment payment, int receiptId);
    
    /**
     * Called when cash payment is confirmed by cashier
     * 
     * @param payment The cash payment
     * @param receivedAmount Amount received from customer
     * @param changeAmount Change to return
     */
    void onCashPaymentConfirmed(Payment payment, double receivedAmount, double changeAmount);
    
    /**
     * Called when transfer payment is verified
     * 
     * @param payment The transfer payment
     * @param transactionId Transaction ID from bank
     */
    void onTransferPaymentVerified(Payment payment, String transactionId);
}