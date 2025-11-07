package com.gdsa.kiosk.interfaces;

import java.util.List;

public interface ReceiptDbRepository {
    long save(
            List<String> lines,
            String customerName,
            String subtotal,
            String tax,
            String total) throws Exception;
}