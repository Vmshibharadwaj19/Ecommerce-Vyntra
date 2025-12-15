package com.ecommerce.repositories;

import com.ecommerce.entities.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByTransactionId(String transactionId);
    
    List<PaymentTransaction> findByPaymentId(Long paymentId);
    
    List<PaymentTransaction> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);
    
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.payment.order.id = :orderId ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT COUNT(pt) FROM PaymentTransaction pt WHERE pt.status = :status AND pt.createdAt BETWEEN :startDate AND :endDate")
    Long countByStatusAndDateRange(
        @Param("status") PaymentTransaction.TransactionStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT SUM(pt.amount) FROM PaymentTransaction pt WHERE pt.type = :type AND pt.status = 'SUCCESS' AND pt.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumAmountByTypeAndDateRange(
        @Param("type") PaymentTransaction.TransactionType type,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}



