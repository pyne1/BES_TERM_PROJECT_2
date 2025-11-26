<%@ page import="model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Details - YorkU Thrift Store</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 40px;
        }
        .container {
            max-width: 700px;
            margin: auto;
            background: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #E31837;
            margin-bottom: 10px;
        }
        .price {
            font-size: 22px;
            font-weight: bold;
            margin-top: 10px;
        }
        .meta {
            margin-top: 10px;
            color: #555;
        }
        .btn-link {
            display: inline-block;
            margin-top: 15px;
            padding: 8px 14px;
            border-radius: 4px;
            border: 1px solid #000;
            background: #E31837;
            color: #fff;
            text-decoration: none;
        }
        .btn-link:hover {
            background: #C41230;
        }
    </style>
</head>
<body>
<div class="container">
	<%@ include file="cartIcon.jsp" %>
    <%
        Product p = (Product) request.getAttribute("product");
        if (p == null) {
    %>
        <p>Product not found.</p>
        <p><a href="catalog">Back to Catalog</a></p>
    <%
        } else {
    %>
        <h1><%= p.getName() %></h1>
        <img src="<%= p.getImageUrl() %>" 
        alt="<%= p.getName() %>" 
        style="max-width:250px; border-radius:8px; margin:20px 0;">
        
        <p class="meta">
            Category: <b><%= p.getCategory() %></b> |
            Brand: <b><%= p.getBrand() %></b> |
            Item ID: <%= p.getItemID() %>
        </p>

        <p><%= p.getDescription() %></p>

        <p class="price">$<%= String.format("%.2f", p.getPrice()) %></p>
        <p>Quantity remaining: <b><%= p.getQuantity() %></b></p>

        <a class="btn-link" href="cart?todo=add&itemID=<%= p.getItemID() %>">
            Add to Cart
        </a>
        <a class="btn-link" href="catalog">Back to Catalog</a>
    <%
        }
    %>
</div>
</body>
</html>
