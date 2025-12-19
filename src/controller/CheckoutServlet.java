package controller;

import dao.OrderDAO;
import model.Cart;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import dao.CustomerDAO;


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
        
        Customer fresh = CustomerDAO.findByEmail(customer.getEmail());
        if (fresh != null) {
            customer = fresh;
            session.setAttribute("currentCustomer", fresh);
        }


        // If account info missing, send them to Account page to fill it in
        if (!hasRequiredAccountInfo(customer)) {
            session.setAttribute(
                    "accountMsg",
                    "Please complete your billing, shipping, and payment information before checking out."
            );
            response.sendRedirect(request.getContextPath() + "/account");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        // Keep payment approval logic (no billing/shipping fields in checkout anymore)
        boolean approved = approvePayment();
        if (!approved) {
            request.setAttribute("paymentError", "CC Authorization Failed.");
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        boolean ok = OrderDAO.saveOrder(customer.getEmail(), cart);
        if (!ok) {
            request.setAttribute("paymentError",
                    "Some items are no longer in stock. Please review your cart and try again.");
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        session.removeAttribute("cart");
        session.setAttribute("cartCount", 0);
        clearCartCookie(request, response);
        
     // make confirmation page not show null
        request.setAttribute("shippingName", customer.getFirstName() + " " + customer.getLastName());
        request.setAttribute("shippingAddress", customer.getShippingAddress());

        request.setAttribute("billingName", customer.getFirstName() + " " + customer.getLastName());
        request.setAttribute("billingAddress", customer.getBillingAddress());

        // so order summary shows
        request.setAttribute("cart", cart);



        request.getRequestDispatcher("orderConfirmation.jsp").forward(request, response);
    }

    private boolean hasRequiredAccountInfo(Customer c) {
        return c != null
                && c.getBillingAddress() != null && !c.getBillingAddress().trim().isEmpty()
                && c.getBillingCity() != null && !c.getBillingCity().trim().isEmpty()
                && c.getBillingPostal() != null && !c.getBillingPostal().trim().isEmpty()
                && c.getShippingAddress() != null && !c.getShippingAddress().trim().isEmpty()
                && c.getShippingCity() != null && !c.getShippingCity().trim().isEmpty()
                && c.getShippingPostal() != null && !c.getShippingPostal().trim().isEmpty()
                && c.getCreditCardNumber() != null && !c.getCreditCardNumber().trim().isEmpty()
                && c.getCreditCardExpiry() != null && !c.getCreditCardExpiry().trim().isEmpty()
                && c.getCreditCardCVV() != null && !c.getCreditCardCVV().trim().isEmpty();
    }
}

