package com.example.Izikwen.repository;

import com.example.Izikwen.entity.WalletAddress;
import com.example.Izikwen.enums.Network;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletAddressRepository extends JpaRepository<WalletAddress, Long> {
    Optional<WalletAddress> findByUserIdAndNetwork(Long userId, Network network);
}