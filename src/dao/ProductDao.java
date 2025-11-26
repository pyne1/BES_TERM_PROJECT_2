package dao;

import model.Product;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // Maps a SQL row to a Product object
    private static Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setItemID(String.valueOf(rs.getInt("product_id")));  // itemID = product_id
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setBrand(rs.getString("brand"));
        p.setCategory(rs.getString("category"));
        p.setQuantity(rs.getInt("stock"));                     // stock = quantity
        p.setPrice(rs.getDouble("price"));
        p.setImageUrl(rs.getString("image_url"));
        return p;
    }

    // =====================
    //   GET ALL PRODUCTS
    // =====================
    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();

        String sql =
            "SELECT p.product_id, p.name, p.description, p.price, " +
            "b.name AS brand, c.name AS category, p.stock, p.image_url " +
            "FROM products p " +
            "JOIN brands b ON p.brand_id = b.brand_id " +
            "JOIN categories c ON p.category_id = c.category_id";

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

    // =====================
    //   GET BY BRAND
    // =====================
    public static List<Product> getByBrand(String brand) {
        List<Product> list = new ArrayList<>();

        String sql =
            "SELECT p.product_id, p.name, p.description, p.price, " +
            "b.name AS brand, c.name AS category, p.stock, p.image_url " +
            "FROM products p " +
            "JOIN brands b ON p.brand_id = b.brand_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE b.name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brand);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =====================
    //   GET BY CATEGORY
    // =====================
    public static List<Product> getByCategory(String category) {
        List<Product> list = new ArrayList<>();

        String sql =
            "SELECT p.product_id, p.name, p.description, p.price, " +
            "b.name AS brand, c.name AS category, p.stock, p.image_url " +
            "FROM products p " +
            "JOIN brands b ON p.brand_id = b.brand_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE c.name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =====================
    //   GET BY PRODUCT ID
    // =====================
    public static Product getById(String itemID) {
        String sql =
            "SELECT p.product_id, p.name, p.description, p.price, " +
            "b.name AS brand, c.name AS category, p.stock, p.image_url " +
            "FROM products p " +
            "JOIN brands b ON p.brand_id = b.brand_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE p.product_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(itemID));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =====================
    //   GET ALL BRANDS
    // =====================
    public static List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();

        String sql = "SELECT name FROM brands ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                brands.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    // =====================
    //   GET ALL CATEGORIES
    // =====================
    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();

        String sql = "SELECT name FROM categories ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}

