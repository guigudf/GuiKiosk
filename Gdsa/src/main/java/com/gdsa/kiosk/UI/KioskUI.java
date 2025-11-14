package com.gdsa.kiosk.UI;

import com.gdsa.kiosk.interfaces.CatalogRepository;
import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.model.MenuItem;
import com.gdsa.kiosk.repo.InMemoryCatalogRepository;
import com.gdsa.kiosk.repo.SqliteReceiptRepository;
import com.gdsa.kiosk.model.ReceiptDbSaver;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;


public class KioskUI extends JFrame {

    private final CatalogRepository catalog = new InMemoryCatalogRepository();
    private final Cart cart = new Cart();
    private final ReceiptService receiptService =
            new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
    private final SqliteReceiptRepository dbRepo = new SqliteReceiptRepository(Paths.get("receipts.db"));
    private final ReceiptDbSaver dbSaver = new ReceiptDbSaver(receiptService, dbRepo);
    private final MenuTableModel cartModel = new MenuTableModel();
    private final JLabel subtotalLabel = new JLabel("Subtotal: $0.00");
    private final JLabel taxLabel = new JLabel("Tax: $0.00");
    private final JLabel totalLabel = new JLabel("Total: $0.00");

    public KioskUI() {
        setTitle("Kiosk - Point of Sale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(8, 8, 8, 8));

        // ===== MENU PANEL =====
        JPanel menuPanel = new JPanel(new BorderLayout(4, 4));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        JPanel itemsGrid = new JPanel(new GridLayout(0, 2, 8, 8));

        List<MenuItem> items = catalog.all();
        for (MenuItem mi : items) {
            JButton b = new JButton("<html><center>" + mi.getName() + "<br>$" + mi.getPrice() + "</center></html>");
            b.setFont(b.getFont().deriveFont(18f));
            b.addActionListener(e -> addToCart(mi, 1));
            itemsGrid.add(b);
        }
        menuPanel.add(new JScrollPane(itemsGrid), BorderLayout.CENTER);

        // ===== CART PANEL =====
        JPanel cartPanel = new JPanel(new BorderLayout(4, 4));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        JTable table = new JTable(cartModel);
        table.setRowHeight(28);
        cartPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== CART BUTTONS =====
        JPanel cartControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton removeBtn = new JButton("Remove Selected");
        JButton clearBtn = new JButton("Clear Cart");
        JButton checkoutBtn = new JButton("Checkout / Receipt");

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String name = cartModel.getNameAt(row);
                cart.remove(name);
                cartModel.refresh();
                updateTotals();
            }
        });

        clearBtn.addActionListener(e -> {
            cart.clear();
            cartModel.refresh();
            updateTotals();
        });

        checkoutBtn.addActionListener(e -> {
            try {
                if (cart.items().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 1️⃣ Render and display receipt
                List<String> receipt = receiptService.render(cart);
                JTextArea ta = new JTextArea(String.join("\n", receipt));
                ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                ta.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                        "Receipt", JOptionPane.INFORMATION_MESSAGE);

                // 2️⃣ Ask for customer name (optional)
                String name = JOptionPane.showInputDialog(this, "Enter customer name:", "Guest");
                if (name == null || name.isBlank()) name = "Guest";

                // 3️⃣ Save to SQLite DB
                long id = dbSaver.renderAndSave(cart, name);

                // 4️⃣ Confirm
                JOptionPane.showMessageDialog(this, "Saved receipt with ID: " + id, "Database Saved",
                        JOptionPane.INFORMATION_MESSAGE);

                // 5️⃣ Clear cart after checkout
                cart.clear();
                cartModel.refresh();
                updateTotals();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving receipt: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cartControls.add(removeBtn);
        cartControls.add(clearBtn);
        cartControls.add(checkoutBtn);
        cartPanel.add(cartControls, BorderLayout.SOUTH);

        // ===== TOTALS PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout(4, 4));
        JPanel totals = new JPanel(new GridLayout(0, 1));
        totals.setBorder(BorderFactory.createTitledBorder("Totals"));
        subtotalLabel.setFont(subtotalLabel.getFont().deriveFont(16f));
        taxLabel.setFont(taxLabel.getFont().deriveFont(16f));
        totalLabel.setFont(totalLabel.getFont().deriveFont(18f).deriveFont(Font.BOLD));
        totals.add(subtotalLabel);
        totals.add(taxLabel);
        totals.add(totalLabel);

        JPanel actions = new JPanel(new GridLayout(0, 1, 4, 4));
        JButton increaseQty = new JButton("Increase Qty");
        JButton decreaseQty = new JButton("Decrease Qty");
        increaseQty.addActionListener(e -> changeQty(table, +1));
        decreaseQty.addActionListener(e -> changeQty(table, -1));
        actions.add(increaseQty);
        actions.add(decreaseQty);

        rightPanel.add(totals, BorderLayout.NORTH);
        rightPanel.add(actions, BorderLayout.CENTER);

        // ===== MAIN LAYOUT =====
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, cartPanel);
        centerSplit.setResizeWeight(0.4);
        root.add(centerSplit, BorderLayout.CENTER);
        root.add(rightPanel, BorderLayout.EAST);

        add(root);
        updateTotals();
    }

    private void addToCart(MenuItem item, int qty) {
        cart.add(item, 1);  //TODO: make custom quantity
        //cart.add(item, qty);
        cartModel.refresh();
        updateTotals();
    }

    private void changeQty(JTable table, int delta) {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String name = cartModel.getNameAt(row);
        for (CartItem ci : cart.items()) {
            if (ci.getItem().getName().equals(name)) {
                int newQty = ci.getQuantity() + delta;
                if (newQty <= 0) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Quantity will become 0 — remove item?",
                            "Remove?", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) cart.remove(name);
                } else {
                    ci.setQuantity(newQty);
                }
                cartModel.refresh();
                updateTotals();
                return;
            }
        }
    }

    private void updateTotals() {
        BigDecimal sub = cart.getSubtotal();
        BigDecimal tax = new FlatRateTaxCalculator(new BigDecimal("0.06")).tax(sub);
        BigDecimal total = sub.add(tax);
        subtotalLabel.setText(String.format("Subtotal: $%.2f", sub));
        taxLabel.setText(String.format("Tax: $%.2f", tax));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KioskUI ui = new KioskUI();
            ui.setVisible(true);
        });
    }

    // ===== INNER CLASS: CART TABLE MODEL =====
    private class MenuTableModel extends AbstractTableModel {
        private final String[] cols = {"Item", "Qty", "Price", "Line Total"};
        private List<CartItem> rows = List.of();

        public void refresh() {
            rows = cart.items();
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int col) { return cols[col]; }
        @Override public Object getValueAt(int r, int c) {
            CartItem ci = rows.get(r);
            return switch (c) {
                case 0 -> ci.getItem().getName();
                case 1 -> ci.getQuantity();
                case 2 -> String.format("$%.2f", ci.getItem().getPrice());
                case 3 -> String.format("$%.2f", ci.lineTotal());
                default -> "";
            };
        }

        public String getNameAt(int r) {
            return rows.get(r).getItem().getName();
        }
    }
}
