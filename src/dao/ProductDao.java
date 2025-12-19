package dao;

import model.Product;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        int pid = rs.getInt("product_id");
        p.setProductId(pid);
        p.setItemID(String.valueOf(pid));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setQuantity(rs.getInt("stock"));
        try { p.setBrand(rs.getString("brand_name")); } catch (SQLException ignored) {}
        try { p.setCategory(rs.getString("category_name")); } catch (SQLException ignored) {}
        return p;
    }

    public static Product getById(String id) {
        String sql =
                "SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, " +
                "b.name AS brand_name, c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "WHERE p.product_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getStockById(String id) {
        String sql = "SELECT stock FROM products WHERE product_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("stock");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static List<Product> getAllProducts() {
        String sql =
                "SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, " +
                "b.name AS brand_name, c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN categories c ON p.category_id = c.category_id";

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Product> getAllProducts(String sort) {
        String orderBy = buildOrderBy(sort);

        String sql =
                "SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, " +
                "b.name AS brand_name, c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                orderBy;

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT name FROM brands ORDER BY name ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) brands.add(rs.getString("name"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return brands;
    }

    public static List<String> getAllCategories() {
        List<String> cats = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) cats.add(rs.getString("name"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cats;
    }

    public static List<Product> getByBrand(String brandName, String sort) {
        String orderBy = buildOrderBy(sort);

        String sql =
                "SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, " +
                "b.name AS brand_name, c.name AS category_name " +
                "FROM products p " +
                "JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "WHERE b.name = ? " +
                orderBy;

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brandName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Product> getByCategory(String categoryName, String sort) {
        String orderBy = buildOrderBy(sort);

        String sql =
                "SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, " +
                "b.name AS brand_name, c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "WHERE c.name = ? " +
                orderBy;

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Product> search(String q, String type, String genre, String sort) {
        String orderBy = buildOrderBy(sort);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.product_id, p.name, p.description, p.price, p.image_url, p.stock, ");
        sql.append("b.name AS brand_name, c.name AS category_name ");
        sql.append("FROM products p ");
        sql.append("LEFT JOIN brands b ON p.brand_id = b.brand_id ");
        sql.append("LEFT JOIN categories c ON p.category_id = c.category_id ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String like = "%" + q.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }

        if (type != null && !type.trim().isEmpty() && !"-- any --".equalsIgnoreCase(type.trim())) {
            sql.append("AND LOWER(c.name) = ? ");
            params.add(type.trim().toLowerCase());
        }

        if (genre != null && !genre.trim().isEmpty() && !"-- any --".equalsIgnoreCase(genre.trim())) {
            sql.append("AND LOWER(c.name) = ? ");
            params.add(genre.trim().toLowerCase());
        }

        sql.append(orderBy);

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addProduct(Product p) {
        String sql =
                "INSERT INTO products (name, description, price, brand_id, category_id, image_url, stock) " +
                "VALUES (?, ?, ?, " +
                "(SELECT brand_id FROM brands WHERE name = ?), " +
                "(SELECT category_id FROM categories WHERE name = ?), " +
                "?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getBrand());
            ps.setString(5, p.getCategory());
            ps.setString(6, p.getImageUrl());
            ps.setInt(7, p.getQuantity());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateProduct(Product p) {
        String sql =
                "UPDATE products SET name=?, description=?, price=?, " +
                "brand_id=(SELECT brand_id FROM brands WHERE name=?), " +
                "category_id=(SELECT category_id FROM categories WHERE name=?), " +
                "image_url=?, stock=? " +
                "WHERE product_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getBrand());
            ps.setString(5, p.getCategory());
            ps.setString(6, p.getImageUrl());
            ps.setInt(7, p.getQuantity());
            ps.setInt(8, p.getProductId());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String buildOrderBy(String sort) {
        if (sort == null) return "";
        String s = sort.trim().toLowerCase();
        if ("price_asc".equals(s) || "price".equals(s) || "asc".equals(s)) return " ORDER BY p.price ASC";
        if ("price_desc".equals(s) || "desc".equals(s)) return " ORDER BY p.price DESC";
        if ("name_asc".equals(s) || "name".equals(s)) return " ORDER BY p.name ASC";
        if ("name_desc".equals(s)) return " ORDER BY p.name DESC";
        return "";
    }
}
