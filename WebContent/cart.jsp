<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Cart - YorkU Thrift Store</title>
    <link rel="stylesheet" href="style.css">

    <style>
        .logout-container {
            position: absolute;
            top: 20px;
            right: 20px;
        }
        .logout-btn {
            padding: 8px 14px;
            background: #e31837;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
        }
        .logout-btn:hover {
            background: #b6122d;
        }
    </style>
</head>
<body>
<%@ include file="logout.jsp" %>
<%@ include file="adminbutton.jsp" %>



<div class="logout-container">
    <a href="logout" class="logout-btn">Logout</a>
</div>

<div class="container">

    <h1>Your Shopping Cart</h1>

    <%
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
    %>
        <p>Your cart is empty.</p>
        <a href="catalog" class="btn">Continue Shopping</a>
    </div>
</body>
</html>
    <%
            return;
        }

        Collection<CartItem> items = cart.getItems();
    %>

    <table class="cart-table">
        <tr>
            <th>Item</th>
            <th>Brand</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Total</th>
            <th>Action</th>
        </tr>

        <%
            for (CartItem ci : items) {
                String id = ci.getProduct().getItemID();
        %>

        <tr>
            <td><%= ci.getProduct().getName() %></td>
            <td><%= ci.getProduct().getBrand() %></td>
            <td>$<%= String.format("%.2f", ci.getProduct().getPrice()) %></td>

            <td>
                <form action="cart" method="get" class="qty-form">
                    <input type="hidden" name="todo" value="update">
                    <input type="hidden" name="itemID" value="<%= id %>">
                    <input type="number" name="qty"
       				value="<%= ci.getQuantity() %>"
       				min="1"
       				max="<%= ci.getProduct().getQuantity() %>">

                    <button type="submit">Update</button>
                </form>
            </td>

            <td>$<%= String.format("%.2f", ci.getTotalPrice()) %></td>

            <td>
                <a href="cart?todo=remove&itemID=<%= id %>" class="remove">Remove</a>
            </td>
        </tr>

        <% } %>

    </table>

    <h2 class="total">Total: $<%= String.format("%.2f", cart.getTotal()) %></h2>

    <div class="actions">
        <a href="catalog" class="btn">Continue Shopping</a>
        <a href="checkout" class="checkout-btn">Checkout</a>
    </div>

</div>

</body>
</html>
