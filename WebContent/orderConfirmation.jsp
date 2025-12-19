<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Confirmed - Everything YorkU</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="logout.jsp" %>


<div class="container">
    <h1>Order Confirmed</h1>
    <p>Thank you for your order. Your payment has been approved.</p>

    <h2>Shipping To</h2>
    <p>
        <strong><%= request.getAttribute("shippingName") %></strong><br>
        <%= request.getAttribute("shippingAddress") %>
    </p>

    <h2>Billing To</h2>
    <p>
        <strong><%= request.getAttribute("billingName") %></strong><br>
        <%= request.getAttribute("billingAddress") %>
    </p>

    <h2>Order Summary</h2>
    <%
        Cart cart = (Cart) request.getAttribute("cart");
        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
    %>
    <table style="margin: 0 auto;">
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
    <p style="margin-top:10px; font-weight:bold;">
        Total Paid: $<%= String.format("%.2f", cart.getTotal()) %>
    </p>
    <%
        }
    %>

    <p style="margin-top:20px;">
        <a href="catalog?view=all" class="btn-link">Back to Catalog</a>
    </p>
</div>
</body>
</html>
