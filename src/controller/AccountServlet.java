package controller;

import dao.CustomerDAO;
import model.Customer;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import dao.OrderDAO;
import model.Order;
import java.util.List;



@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Customer current = (session != null) ? (Customer) session.getAttribute("currentCustomer") : null;

        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Reload from DB so the form always shows latest data
        Customer fresh = CustomerDAO.findByEmail(current.getEmail());
        if (fresh != null) {
            session.setAttribute("currentCustomer", fresh);
            current = fresh;
        }

        List<Order> orders = OrderDAO.getOrdersByEmail(current.getEmail());
        req.setAttribute("orders", orders);
        
        req.setAttribute("customer", current);
        req.getRequestDispatcher("account.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Customer current = (session != null) ? (Customer) session.getAttribute("currentCustomer") : null;

        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        // Update fields
        current.setFirstName(req.getParameter("firstName"));
        current.setLastName(req.getParameter("lastName"));

        // Optional: allow changing email (your call). If you do, check uniqueness.
        // current.setEmail(req.getParameter("email"));

        current.setCreditCardNumber(req.getParameter("creditCardNumber"));
        current.setCreditCardExpiry(req.getParameter("creditCardExpiry"));
        current.setCreditCardCVV(req.getParameter("creditCardCVV"));

        current.setBillingAddress(req.getParameter("billingAddress"));
        current.setBillingCity(req.getParameter("billingCity"));
        current.setBillingPostal(req.getParameter("billingPostal"));

        current.setShippingAddress(req.getParameter("shippingAddress"));
        current.setShippingCity(req.getParameter("shippingCity"));
        current.setShippingPostal(req.getParameter("shippingPostal"));

        boolean ok = CustomerDAO.updateCustomer(current);

        if (!ok) {
            req.setAttribute("error", "Could not update account info. Please try again.");
            req.setAttribute("customer", current);
            req.getRequestDispatcher("account.jsp").forward(req, resp);
            return;
        }

        // keep session updated
        session.setAttribute("currentCustomer", current);
        resp.sendRedirect(req.getContextPath() + "/account");
    }
}

