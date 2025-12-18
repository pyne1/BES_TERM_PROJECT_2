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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        Customer customer = (Customer) session.getAttribute("currentCustomer");
        if (customer == null) {
            session.setAttribute("redirectAfterLogin", "checkout");
            response.sendRedirect(request.getContextPath() + "/login");
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
            session.setAttribute("redirectAfterLogin", "checkout");
            response.sendRedirect(request.getContextPath() + "/login");
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

        boolean approved = approvePayment();

        if (!approved) {
            request.setAttribute("billingName", billingName);
            request.setAttribute("billingAddress", billingAddress);
            request.setAttribute("shippingName", shippingName);
            request.setAttribute("shippingAddress", shippingAddress);
            request.setAttribute("cardNumber", cardNumber);
            request.setAttribute("cardExpiry", cardExpiry);
            request.setAttribute("cardCvv", cardCvv);
            request.setAttribute("paymentError", "CC Authorization Failed.");
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        OrderDAO.saveOrder(customer.getEmail(), cart);

        request.setAttribute("billingName", billingName);
        request.setAttribute("billingAddress", billingAddress);
        request.setAttribute("shippingName", shippingName);
        request.setAttribute("shippingAddress", shippingAddress);
        request.setAttribute("cart", cart);

        session.removeAttribute("cart");
        session.setAttribute("cartCount", 0);

        request.getRequestDispatcher("orderConfirmation.jsp").forward(request, response);
    }
}
