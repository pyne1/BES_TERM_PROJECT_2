package controller;

import dao.OrderDAO;
import model.Cart;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static int paymentCounter = 0;

    private synchronized boolean approvePayment() {
        paymentCounter++;
        return paymentCounter % 3 != 0;
    }

    private void clearCartCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = new Cookie("cart", "");
        String path = req.getContextPath();
        if (path == null || path.isEmpty()) path = "/";
        cookie.setPath(path);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        Customer customer = (Customer) session.getAttribute("currentCustomer");
        if (customer == null) {
            request.getRequestDispatcher("forceLogin.jsp").forward(request, response);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        Customer customer = (Customer) session.getAttribute("currentCustomer");
        if (customer == null) {
            request.getRequestDispatcher("forceLogin.jsp").forward(request, response);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        String billingName = request.getParameter("billingName");
        String billingAddress = request.getParameter("billingAddress");
        String shippingName = request.getParameter("shippingName");
        String shippingAddress = request.getParameter("shippingAddress");
        String cardNumber = request.getParameter("cardNumber");
        String cardExpiry = request.getParameter("cardExpiry");
        String cardCvv = request.getParameter("cardCvv");

        request.setAttribute("billingName", billingName);
        request.setAttribute("billingAddress", billingAddress);
        request.setAttribute("shippingName", shippingName);
        request.setAttribute("shippingAddress", shippingAddress);
        request.setAttribute("cardNumber", cardNumber);
        request.setAttribute("cardExpiry", cardExpiry);
        request.setAttribute("cardCvv", cardCvv);
        request.setAttribute("cart", cart);

        boolean approved = approvePayment();
        if (!approved) {
            request.setAttribute("paymentError", "CC Authorization Failed.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        boolean ok = OrderDAO.saveOrder(customer.getEmail(), cart);
        if (!ok) {
            request.setAttribute("paymentError",
                    "Some items are no longer in stock. Please review your cart and try again.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        session.removeAttribute("cart");
        session.setAttribute("cartCount", 0);
        clearCartCookie(request, response);

        request.getRequestDispatcher("orderConfirmation.jsp").forward(request, response);
    }
}
