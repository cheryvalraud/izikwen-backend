package com.example.Izikwen.repository;

import com.example.Izikwen.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOrder_User_Id(Long userId);

}
