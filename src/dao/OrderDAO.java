package dao;

import model.Cart;
import model.CartItem;
import model.Order;
import model.OrderItem;
import model.Product;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customerEmail, total, orderDate FROM orders ORDER BY orderDate DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                int orderId = rs.getInt("id");

                o.setId(orderId);
                o.setCustomerEmail(rs.getString("customerEmail"));
                o.setTotal(rs.getDouble("total"));
                o.setOrderDate(rs.getTimestamp("orderDate"));
                o.setItems(getItemsForOrder(conn, orderId));

                orders.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private static List<OrderItem> getItemsForOrder(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql =
                "SELECT product_id, quantity, unit_price " +
                "FROM order_items WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setItemID(String.valueOf(rs.getInt("product_id")));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("unit_price"));
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static boolean saveOrder(String customerEmail, Cart cart) {

        String decSql =
                "UPDATE products SET stock = stock - ? " +
                "WHERE product_id = ? AND stock >= ?";

        String orderSql =
                "INSERT INTO orders (customerEmail, total, orderDate) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP)";

        String itemSql =
                "INSERT INTO order_items " +
                "(order_id, product_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDec = conn.prepareStatement(decSql)) {
                for (CartItem ci : cart.getItems()) {
                    Product p = ci.getProduct();
                    int qty = ci.getQuantity();
                    int productId = p.getProductId();

                    System.out.println(
                        "DEC: productId=" + productId +
                        " itemID=" + p.getItemID() +
                        " qty=" + qty +
                        " name=" + p.getName()
                    );

                    psDec.setInt(1, qty);
                    psDec.setInt(2, productId);
                    psDec.setInt(3, qty);

                    if (psDec.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            int orderId;
            try (PreparedStatement psOrder =
                         conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {

                psOrder.setString(1, customerEmail);
                psOrder.setDouble(2, cart.getTotal());
                psOrder.executeUpdate();

                try (ResultSet keys = psOrder.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }
                    orderId = keys.getInt(1);
                }
            }

            try (PreparedStatement psItem = conn.prepareStatement(itemSql)) {
                for (CartItem ci : cart.getItems()) {
                    Product p = ci.getProduct();
                    int qty = ci.getQuantity();
                    double price = p.getPrice();

                    psItem.setInt(1, orderId);
                    psItem.setInt(2, p.getProductId());
                    psItem.setInt(3, qty);
                    psItem.setDouble(4, price);
                    psItem.setDouble(5, qty * price);

                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ignored) {}
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }
}
