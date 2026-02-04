package com.example.Izikwen.repository;

import com.example.Izikwen.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}