package controller;

import dao.ProductDao;
import model.Cart;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.http.Cookie;
import java.util.StringTokenizer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;




@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("currentCustomer") : null;

        //user must be logged in 
        if (user == null) {
            req.getRequestDispatcher("forceLogin.jsp").forward(req, resp);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = loadCartFromCookie(req);
            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", cart.getTotalQuantity());
        }

        
     // Refresh stock values in cart items so UI max is correct
        for (model.CartItem ci : cart.getItems()) {
            String pid = ci.getProduct().getItemID();
            int latestStock = ProductDao.getStockById(pid);
            ci.getProduct().setQuantity(latestStock); // Product.quantity is used as stock in your model
        }


        String todo = req.getParameter("todo");

        if (todo == null) {
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
            return;
        }

        switch (todo) {
        case "add":
            String itemID = req.getParameter("itemID");
            Product p = ProductDao.getById(itemID);

            if (p != null) {
                int stock = p.getQuantity();              // comes from p.stock in DB
                int currentQty = cart.getQuantity(itemID);
                int requestedQty = currentQty + 1;

                if (stock <= 0) {
                    session.setAttribute("cartMsg", "Out of stock.");
                } else if (requestedQty > stock) {
                    session.setAttribute("cartMsg", "Cannot add more than available stock (" + stock + ").");
                } else {
                    cart.addItem(p);
                    session.removeAttribute("cartMsg");
                }
            }

            session.setAttribute("cartCount", cart.getTotalQuantity());
            saveCartToCookie(req, cart, resp);
            String referer = req.getHeader("referer");
            resp.sendRedirect(referer);
            break;


                
                
                
            case "remove":
                cart.removeItem(req.getParameter("itemID"));
                session.setAttribute("cartCount", cart.getTotalQuantity());
                saveCartToCookie(req, cart, resp);
                resp.sendRedirect("cart");
                break;
                
                

            case "update":
                String id = req.getParameter("itemID");
                int qty = Integer.parseInt(req.getParameter("qty"));

                int stock = ProductDao.getStockById(id);

                if (stock <= 0) {
                    cart.removeItem(id);
                    session.setAttribute("cartMsg", "Item is now out of stock and was removed.");
                } else {
                    if (qty < 1) qty = 1;
                    if (qty > stock) {
                        qty = stock;
                        session.setAttribute("cartMsg", "Quantity reduced to available stock (" + stock + ").");
                    } else {
                        session.removeAttribute("cartMsg");
                    }
                    cart.updateQuantity(id, qty);
                }

                session.setAttribute("cartCount", cart.getTotalQuantity());
                saveCartToCookie(req, cart, resp);
                resp.sendRedirect("cart");
                break;


                
                
            default:
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
        }
    }
    
    private void saveCartToCookie(HttpServletRequest req, Cart cart, HttpServletResponse resp) {
        StringBuilder sb = new StringBuilder();

        for (model.CartItem ci : cart.getItems()) {
            sb.append(ci.getProduct().getItemID())
              .append(":")
              .append(ci.getQuantity())
              .append("|"); // safe separator (not comma)
        }

        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);

        String encoded = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);

        Cookie cookie = new Cookie("cart", encoded);

        String path = req.getContextPath();
        if (path == null || path.isEmpty()) path = "/";
        cookie.setPath(path);

        cookie.setMaxAge(60 * 60 * 24 * 7);
        resp.addCookie(cookie);
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
                    try {
                        qty = Integer.parseInt(pair[1]);
                    } catch (NumberFormatException ex) {
                        continue;
                    }

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
