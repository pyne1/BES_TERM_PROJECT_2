<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Customer" %>

<%
    Customer customer = (Customer) session.getAttribute("currentCustomer");
    Cart cart = (Cart) session.getAttribute("cart");

    String billingNamePrefill = (String) request.getAttribute("billingName");
    String billingAddressPrefill = (String) request.getAttribute("billingAddress");
    String shippingNamePrefill = (String) request.getAttribute("shippingName");
    String shippingAddressPrefill = (String) request.getAttribute("shippingAddress");
    String cardNumberPrefill = (String) request.getAttribute("cardNumber");
    String cardExpiryPrefill = (String) request.getAttribute("cardExpiry");
    String paymentError = (String) request.getAttribute("paymentError");

    if (billingNamePrefill == null && customer != null) {
        billingNamePrefill = customer.getFirstName() + " " + customer.getLastName();
    }
    if (shippingNamePrefill == null && customer != null) {
        shippingNamePrefill = customer.getFirstName() + " " + customer.getLastName();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Checkout - Everything YorkU</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="logout.jsp" %>
<%@ include file="cartIcon.jsp" %>


<div class="container">
    <h1>Checkout</h1>

    <div style="margin-bottom:20px;">
        <h2>Order Summary</h2>
        <%
            if (cart == null || cart.getItems().isEmpty()) {
        %>
            <p>Your cart is empty.</p>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>Item</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Subtotal</th>
            </tr>
            <%
                for (CartItem ci : cart.getItems()) {
            %>
            <tr>
                <td><%= ci.getProduct().getName() %></td>
                <td><%= ci.getQuantity() %></td>
                <td>$<%= String.format("%.2f", ci.getProduct().getPrice()) %></td>
                <td>$<%= String.format("%.2f", ci.getTotalPrice()) %></td>
            </tr>
            <%
                }
            %>
        </table>
        <p style="margin-top:10px; font-weight:bold;">
            Total: $<%= String.format("%.2f", cart.getTotal()) %>
        </p>
        <%
            }
        %>
    </div>

    <h2>Billing and Shipping Information</h2>

    <%
        if (paymentError != null) {
    %>
        <p style="color:red; font-weight:bold;"><%= paymentError %></p>
    <%
        }
    %>

    <form action="checkout" method="post">
        <h3>Billing Information</h3>
        <label>Full Name:</label><br>
        <input type="text" name="billingName"
               value="<%= billingNamePrefill != null ? billingNamePrefill : "" %>"
               required><br><br>

        <label>Billing Address:</label><br>
        <textarea name="billingAddress" rows="3" cols="50" required><%= billingAddressPrefill != null ? billingAddressPrefill : "" %></textarea><br><br>

        <h3>Shipping Information</h3>
        <label>Full Name:</label><br>
        <input type="text" name="shippingName"
               value="<%= shippingNamePrefill != null ? shippingNamePrefill : "" %>"
               required><br><br>

        <label>Shipping Address:</label><br>
        <textarea name="shippingAddress" rows="3" cols="50" required><%= shippingAddressPrefill != null ? shippingAddressPrefill : "" %></textarea><br><br>

        <h3>Payment Information</h3>
        <label>Card Number:</label><br>
        <input type="text" name="cardNumber"
               value="<%= cardNumberPrefill != null ? cardNumberPrefill : "" %>"
               required><br><br>

        <label>Expiry (MM/YY):</label><br>
        <input type="text" name="cardExpiry"
               value="<%= cardExpiryPrefill != null ? cardExpiryPrefill : "" %>"
               required><br><br>

        <label>CVV:</label><br>
        <input type="password" name="cardCvv" required><br><br>

        <button type="submit">Confirm Order</button>
    </form>

    <p style="margin-top:20px;">
        <a href="cart" class="btn-link">Back to Cart</a>
    </p>
</div>
</body>
</html>
