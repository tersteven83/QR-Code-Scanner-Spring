package com.pcop.qrcode_scanner.QrCode;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class QrCodeService {

    @Autowired
    QrCodeRepository qrCodeRepository;

    public QrCode save(QrCode qrCode) throws IOException, WriterException {
        String qrCodePath = generateQrCode(qrCode.getData());
        qrCode.setPath(qrCodePath);
        return qrCodeRepository.save(qrCode);
    }

    public QrCode findByData(String data) {
        return qrCodeRepository.findByData(data);
    }

    public void deleteById(Long id) {
        qrCodeRepository.deleteById(id);
    }

    private String generateQrCode(String data) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        String qrCodePath = "static/qr_codes/" + UUID.randomUUID() + ".png";
        File qrCodeFile = new File("./src/main/resources/" + qrCodePath);
        ImageIO.write(image, "PNG", qrCodeFile);

        return qrCodePath;

    }

    public Optional<QrCode> findById(Long id) {
        return qrCodeRepository.findById(id);
    }

}
