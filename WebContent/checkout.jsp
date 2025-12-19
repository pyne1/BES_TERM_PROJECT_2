<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Customer" %>

<%
    Customer customer = (Customer) session.getAttribute("currentCustomer");
    Cart cart = (Cart) session.getAttribute("cart");
    String paymentError = (String) request.getAttribute("paymentError");
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



<div class="container">
    <h1>Checkout</h1>

    <div style="margin-bottom:20px; max-width: 600px; text-align: center;">
        <h2>Order Summary</h2>
        <%
            if (cart == null || cart.getItems().isEmpty()) {
        %>
            <p>Your cart is empty.</p>
        <%
            } else {
        %>
		<table style="margin: 0 auto; border-collapse: collapse;">
    	<tr>
        	<th style="padding: 8px 20px;">Item</th>
        	<th style="padding: 8px 20px;">Qty</th>
        	<th style="padding: 8px 20px;">Price</th>
        	<th style="padding: 8px 20px;">Subtotal</th>
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
        <p style="margin-top:20px; padding-left:60px; padding-top:10px; font-weight:bold;">
            Total: $<%= String.format("%.2f", cart.getTotal()) %>
        </p>
        <%
            }
        %>
    </div>

    <!-- READ-ONLY ACCOUNT INFO -->
    <hr style="margin:18px 0;">

    <h2>Account Info (On File)</h2>

    <%
        String fullName = (customer == null) ? "" : (customer.getFirstName() + " " + customer.getLastName());

        String shippingLine = (customer != null && customer.getShippingAddress() != null) ? customer.getShippingAddress() : "";
        String shippingCity = (customer != null && customer.getShippingCity() != null) ? customer.getShippingCity() : "";
        String shippingPostal = (customer != null && customer.getShippingPostal() != null) ? customer.getShippingPostal() : "";

        String billingLine = (customer != null && customer.getBillingAddress() != null) ? customer.getBillingAddress() : "";
        String billingCity = (customer != null && customer.getBillingCity() != null) ? customer.getBillingCity() : "";
        String billingPostal = (customer != null && customer.getBillingPostal() != null) ? customer.getBillingPostal() : "";

        String cc = (customer != null && customer.getCreditCardNumber() != null) ? customer.getCreditCardNumber() : "";
        String last4 = (cc.length() >= 4) ? cc.substring(cc.length() - 4) : "";
        String expiry = (customer != null && customer.getCreditCardExpiry() != null) ? customer.getCreditCardExpiry() : "";
    %>

    <div style="padding:10px; border:1px solid #ddd; border-radius:8px; background:#fafafa;">
        <p><strong>Name:</strong> <%= fullName %></p>

        <p><strong>Shipping:</strong><br>
            <%= shippingLine %><br>
            <%= shippingCity %> <%= shippingPostal %>
        </p>

        <p><strong>Billing:</strong><br>
            <%= billingLine %><br>
            <%= billingCity %> <%= billingPostal %>
        </p>

        <p><strong>Payment:</strong><br>
            Card ending in <strong><%= last4 %></strong>
            <% if (expiry != null && !expiry.isEmpty()) { %>
                (Exp: <%= expiry %>)
            <% } %>
        </p>

        <p style="margin-top:10px;">
            <a class="btn-link" href="<%= request.getContextPath() %>/account">Edit in Account</a>
        </p>
    </div>
    <!-- END READ-ONLY ACCOUNT INFO -->

    <%
        if (paymentError != null) {
    %>
        <p style="color:red; font-weight:bold;"><%= paymentError %></p>
    <%
        }
    %>

    <form action="checkout" method="post">
        <button type="submit">Confirm Order</button>
    </form>

    <p style="margin-top:20px;">
        <a href="cart" class="btn-link">Back to Cart</a>
    </p>
</div>
</body>
</html>

