package com.gdsa.kiosk.interfaces;
import java.nio.file.Path;
import java.util.List;

public interface ReceiptRepository {
    Path save(List<String> receiptLines) throws Exception;
}
