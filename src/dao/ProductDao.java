package dao;

import model.Product;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setItemID(String.valueOf(rs.getInt("product_id")));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setBrand(rs.getString("brand_name"));
        p.setCategory(rs.getString("category_name"));
        p.setPrice(rs.getDouble("price"));
        p.setQuantity(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        return p;
    }

    private static final String BASE_SELECT =
            "SELECT p.product_id, p.name, p.description, p.price, p.stock, p.image_url, " +
            "b.name AS brand_name, c.name AS category_name " +
            "FROM products p " +
            "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
            "LEFT JOIN categories c ON p.category_id = c.category_id";

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY p.product_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Product getById(String id) {
        String sql = BASE_SELECT + " WHERE p.product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Product> getByBrand(String brand) {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE b.name = ? ORDER BY p.product_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Product> getByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE c.name = ? ORDER BY p.product_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getAllBrands() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM brands ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getAllCategories() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void addProduct(Product p) {
        String sql = "INSERT INTO products " +
                "(name, description, price, brand_id, category_id, stock) " +
                "VALUES (?, ?, ?, " +
                "(SELECT brand_id FROM brands WHERE name = ?), " +
                "(SELECT category_id FROM categories WHERE name = ?), ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getBrand());
            ps.setString(5, p.getCategory());
            ps.setInt(6, p.getQuantity());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setItemID(String.valueOf(keys.getInt(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(Product p) {
        String sql = "UPDATE products SET " +
                "name = ?, " +
                "description = ?, " +
                "price = ?, " +
                "brand_id = (SELECT brand_id FROM brands WHERE name = ?), " +
                "category_id = (SELECT category_id FROM categories WHERE name = ?), " +
                "stock = ? " +
                "WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getBrand());
            ps.setString(5, p.getCategory());
            ps.setInt(6, p.getQuantity());
            ps.setInt(7, Integer.parseInt(p.getItemID()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int getStockById(String id) {
        String sql = "SELECT stock FROM products WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private static String orderBy(String sort) {
        if ("price_asc".equalsIgnoreCase(sort)) 
            return " ORDER BY p.price ASC";

        if ("price_desc".equalsIgnoreCase(sort)) 
            return " ORDER BY p.price DESC";

        if ("name_asc".equalsIgnoreCase(sort)) 
            return " ORDER BY p.name ASC";

        if ("name_desc".equalsIgnoreCase(sort)) 
            return " ORDER BY p.name DESC";

        return " ORDER BY p.product_id";
    }
    
   //Overloadeded methods for sorting
    public static List<Product> getAllProducts(String sort) {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + orderBy(sort);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public static List<Product> getByBrand(String brand, String sort) {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE b.name = ? " + orderBy(sort);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brand);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Product> getByCategory(String category, String sort) {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE c.name = ? " + orderBy(sort);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }





}
