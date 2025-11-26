<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Catalog - YorkU Thrift Store</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 40px;
        }
        .container {
            max-width: 1000px;
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
        .filters {
            margin-bottom: 20px;
            padding: 10px;
            background: #fafafa;
            border-radius: 8px;
            border: 1px solid #ddd;
        }
        label {
            margin-right: 10px;
        }
        select {
            margin-right: 10px;
            padding: 4px 6px;
        }
        button, .btn-link {
            padding: 6px 10px;
            font-size: 14px;
            border-radius: 4px;
            border: 1px solid #000;
            background: #E31837;
            color: #fff;
            cursor: pointer;
            text-decoration: none;
        }
        button:hover, .btn-link:hover {
            background: #C41230;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
        th {
            background: #E31837;
            color: #fff;
        }
        .price {
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
	<%@ include file="cartIcon.jsp" %>
    <h1>Catalog</h1>
    <p>Browse all products, or filter by brand or category.</p>

    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        List<String> brands = (List<String>) request.getAttribute("brands");
        List<String> categories = (List<String>) request.getAttribute("categories");
        String selectedView = (String) request.getAttribute("selectedView");
        String selectedBrand = (String) request.getAttribute("selectedBrand");
        String selectedCategory = (String) request.getAttribute("selectedCategory");
        if (selectedView == null) selectedView = "all";
    %>

    <div class="filters">
        <form action="catalog" method="get" style="display:inline;">
            <input type="hidden" name="view" value="all">
            <button type="submit">View All</button>
        </form>

        <form action="catalog" method="get" style="display:inline;">
            <label for="brandSelect">By Brand:</label>
            <input type="hidden" name="view" value="brand">
            <select name="brand" id="brandSelect">
                <option value="">-- choose brand --</option>
                <%
                    if (brands != null) {
                        for (String b : brands) {
                            String sel = (b.equals(selectedBrand)) ? "selected" : "";
                %>
                            <option value="<%= b %>" <%= sel %>><%= b %></option>
                <%
                        }
                    }
                %>
            </select>
            <button type="submit">Filter</button>
        </form>

        <form action="catalog" method="get" style="display:inline;">
            <label for="catSelect">By Category:</label>
            <input type="hidden" name="view" value="category">
            <select name="category" id="catSelect">
                <option value="">-- choose category --</option>
                <%
                    if (categories != null) {
                        for (String c : categories) {
                            String sel = (c.equals(selectedCategory)) ? "selected" : "";
                %>
                            <option value="<%= c %>" <%= sel %>><%= c %></option>
                <%
                        }
                    }
                %>
            </select>
            <button type="submit">Filter</button>
        </form>
    </div>

    <%
        if (products == null || products.isEmpty()) {
    %>
        <p>No products found.</p>
    <%
        } else {
    %>
    <table>
        <tr>
            <th>Item</th>
            <th>Category</th>
            <th>Brand</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Actions</th>
        </tr>
        <%
            for (Product p : products) {
        %>
        <tr>
            <td>
                <a href="product?id=<%= p.getItemID() %>">
                    <%= p.getName() %>
                </a>
            </td>
            <td><%= p.getCategory() %></td>
            <td><%= p.getBrand() %></td>
            <td class="price">$<%= String.format("%.2f", p.getPrice()) %></td>
            <td><%= p.getQuantity() %></td>
            <td>
                <!-- add-to-cart will be handled by CartServlet later -->
                <a class="btn-link" href="cart?todo=add&itemID=<%= p.getItemID() %>">
                    Add to Cart
                </a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        }
    %>

    <p style="margin-top:20px;">
        <a href="index.jsp" class="btn-link">Back to Home</a>
    </p>
</div>
</body>
</html>
