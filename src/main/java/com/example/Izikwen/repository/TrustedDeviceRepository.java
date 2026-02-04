package com.example.Izikwen.repository;

import com.example.Izikwen.entity.TrustedDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrustedDeviceRepository extends JpaRepository<TrustedDevice, Long> {

    Optional<TrustedDevice> findByUserEmailAndDeviceId(String userEmail, String deviceId);

    List<TrustedDevice> findAllByUserEmailOrderByLastUsedAtDesc(String userEmail);

    void deleteByUserEmailAndDeviceId(String userEmail, String deviceId);

    void deleteAllByUserEmail(String userEmail);
}
