package controller;

import dao.ProductDao;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Cart;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/catalog")
public class ProductListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        Object user = session.getAttribute("currentCustomer");

        if (session.getAttribute("cart") == null) {
            Cart cart = new Cart();
            if (user != null) {
                cart = loadCartFromCookie(request);
            }
            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", cart.getTotalQuantity());
        }

        String view = request.getParameter("view");
        String brand = request.getParameter("brand");
        String category = request.getParameter("category");

        String q = request.getParameter("q");

        String sort = request.getParameter("sort");
        if (sort == null) sort = "none";

        List<Product> products;

        boolean hasSearch = (q != null && !q.trim().isEmpty());

        // Decide view + query
        if ("search".equalsIgnoreCase(view) || hasSearch) {
            view = "search";
            // Reuse your existing DAO method: pass null for removed filters
            products = ProductDao.search(q, null, null, sort);

        } else if ("brand".equalsIgnoreCase(view) && brand != null && !brand.isEmpty()) {
            products = ProductDao.getByBrand(brand, sort);

        } else if ("category".equalsIgnoreCase(view) && category != null && !category.isEmpty()) {
            products = ProductDao.getByCategory(category, sort);

        } else {
            view = "all";
            products = ProductDao.getAllProducts(sort);
        }

        request.setAttribute("products", products);
        request.setAttribute("brands", ProductDao.getAllBrands());
        request.setAttribute("categories", ProductDao.getAllCategories());

        request.setAttribute("selectedView", view);
        request.setAttribute("selectedBrand", brand);
        request.setAttribute("selectedCategory", category);
        request.setAttribute("selectedSort", sort);

        request.setAttribute("q", q == null ? "" : q);

        request.getRequestDispatcher("items.jsp").forward(request, response);
    }

    private Cart loadCartFromCookie(HttpServletRequest req) {
        Cart cart = new Cart();
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return cart;

        for (Cookie c : cookies) {
            if ("cart".equals(c.getName())) {
                String decoded = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                if (decoded == null || decoded.trim().isEmpty()) return cart;

                String[] entries = decoded.split("\\|");
                for (String entry : entries) {
                    String[] pair = entry.split(":");
                    if (pair.length != 2) continue;

                    String id = pair[0];
                    int qty;
                    try { qty = Integer.parseInt(pair[1]); }
                    catch (NumberFormatException ex) { continue; }

                    Product p = ProductDao.getById(id);
                    if (p == null) continue;

                    int stock = p.getQuantity();
                    if (stock <= 0) continue;
                    if (qty > stock) qty = stock;

                    cart.addItem(p);
                    cart.updateQuantity(id, qty);
                }
                return cart;
            }
        }
        return cart;
    }
}

