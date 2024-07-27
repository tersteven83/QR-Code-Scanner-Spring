package com.pcop.qrcode_scanner.QrCode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    QrCode findByData(String data);
}
