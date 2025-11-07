package com.gdsa.kiosk.CLI;

import com.gdsa.kiosk.interfaces.CatalogRepository;
import com.gdsa.kiosk.interfaces.ReceiptRepository;
import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.FileReceiptRepository;
import com.gdsa.kiosk.repo.InMemoryCatalogRepository;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class MainCLI  {
    public static void main() {
        CatalogRepository repo = new InMemoryCatalogRepository();
        ReceiptService receipt = new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
        ReceiptRepository fileRepo = new FileReceiptRepository(Paths.get("receipts"));
        ReceiptSaver saver = new ReceiptSaver(receipt, fileRepo);
        var dbRepo = new com.gdsa.kiosk.repo.SqliteReceiptRepository(java.nio.file.Paths.get("receipts.db"));
        var dbSaver = new com.gdsa.kiosk.model.ReceiptDbSaver(receipt, dbRepo);
        Cart cart = new Cart();
        receipt = new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Order Kiosk CLI");
        printHelp();

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();
            try {
                switch (cmd) {
                    case "menu" ->
                            repo.all().forEach(mi -> System.out.println(mi.getCategory() + " - " + mi.getName() + " " + mi.getPrice()));
                    case "menu.cat" -> {
                        // menu.cat COFFEE
                        Category c = Category.valueOf(parts[1].toUpperCase());
                        repo.byCategory(c).forEach(mi -> System.out.println(mi.getName() + " " + mi.getPrice()));
                    }
                    case "add" -> {
                        String name = parts[1];
                        int qty = Integer.parseInt(parts[2]);
                        MenuItem mi = repo.all().stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("unknown item"));
                        cart.add(mi, qty);
                        System.out.println("Added " + qty + " x " + mi.getName());
                    }
                    case "receipt" -> receipt.render(cart).forEach(System.out::println);
                    case "save" -> {
                        Path p = saver.renderAndSave(cart);
                        System.out.println("Saved receipt at " + p.toAbsolutePath());
                    }
                    case "save.db" -> {
                        long id = dbSaver.renderAndSave(cart, parts.length > 1 ? parts[1] : "Guest");
                        System.out.println("Saved receipt row id " + id);
                    }
                    case "help" -> printHelp();
                    case "quit" -> {
                        System.out.println("Bye");
                        return;
                    }
                    default -> System.out.println("Unknown command, type help");
                }
            } catch (Exception e) {
                System.out.println("Error, " + e.getMessage());
            }
        }
    }

    private static void printHelp(){
        System.out.println("Commands, menu | menu.cat <Category> | add <Name> <Qty> | receipt | save | quit");
    }
}