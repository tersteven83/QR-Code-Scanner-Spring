package com.pcop.qrcode_scanner.QrCode;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrCodeService {

    @Autowired
    QrCodeRepository qrCodeRepository;

    public QrCode save(QrCode qrCode) {
        return qrCodeRepository.save(qrCode);
    }

    public QrCode findByData(String data) {
        return qrCodeRepository.findByData(data);
    }

    public void deleteById(Long id) {
        qrCodeRepository.deleteById(id);
    }

}
