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
        select, input[type="text"] {
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
    <%@ include file="logout.jsp" %>
    <%@ include file="cartIcon.jsp" %>

    <h1>Catalog</h1>
<%
    String cartMsg = (String) session.getAttribute("cartMsg");
    if (cartMsg != null) {
%>
    <div style="
        margin: 12px 0;
        padding: 10px 12px;
        border: 1px solid #e31837;
        background: #fdeaea;
        color: #a40000;
        border-radius: 6px;
        font-weight: bold;
    ">
        <%= cartMsg %>
    </div>
<%
        session.removeAttribute("cartMsg"); // show once
    }
%>

    
    <p>Browse all products, or filter by brand or category.</p>

    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        List<String> brands = (List<String>) request.getAttribute("brands");
        List<String> categories = (List<String>) request.getAttribute("categories");

        String selectedView = (String) request.getAttribute("selectedView");
        String selectedBrand = (String) request.getAttribute("selectedBrand");
        String selectedCategory = (String) request.getAttribute("selectedCategory");
        String selectedSort = (String) request.getAttribute("selectedSort");

        String q = (String) request.getAttribute("q");
        String type = (String) request.getAttribute("type");
        String genre = (String) request.getAttribute("genre");

        if (selectedSort == null) selectedSort = "none";
        if (selectedView == null) selectedView = "all";
        if (q == null) q = "";
        if (type == null) type = "";
        if (genre == null) genre = "";
    %>

    <div class="filters">
        <form action="catalog" method="get" style="display:inline;">
            <input type="hidden" name="view" value="all">
            <button type="submit">View All</button>
        </form>

        <form action="catalog" method="get" style="display:inline;">
            <input type="hidden" name="view" value="search">
            <label>Search:</label>
            <input type="text" name="q" placeholder="keyword..." value="<%= q %>">

            <label for="typeSelect">Type:</label>
            <select name="type" id="typeSelect">
                <option value="">-- any --</option>
                <%
                    if (categories != null) {
                        for (String c : categories) {
                            String sel = c.equals(type) ? "selected" : "";
                %>
                            <option value="<%= c %>" <%= sel %>><%= c %></option>
                <%
                        }
                    }
                %>
            </select>

            <label for="genreSelect">Genre:</label>
            <select name="genre" id="genreSelect">
                <option value="">-- any --</option>
                <%
                    if (brands != null) {
                        for (String b : brands) {
                            String sel = b.equals(genre) ? "selected" : "";
                %>
                            <option value="<%= b %>" <%= sel %>><%= b %></option>
                <%
                        }
                    }
                %>
            </select>

            <input type="hidden" name="sort" value="<%= selectedSort %>">
            <button type="submit">Search</button>
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
            <input type="hidden" name="sort" value="<%= selectedSort %>">
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
            <input type="hidden" name="sort" value="<%= selectedSort %>">
            <button type="submit">Filter</button>
        </form>

        <form action="catalog" method="get" style="display:inline;">
            <label for="sortSelect">Sort:</label>
            <input type="hidden" name="view" value="<%= selectedView %>">
            <input type="hidden" name="brand" value="<%= selectedBrand == null ? "" : selectedBrand %>">
            <input type="hidden" name="category" value="<%= selectedCategory == null ? "" : selectedCategory %>">
            <input type="hidden" name="q" value="<%= q %>">
            <input type="hidden" name="type" value="<%= type %>">
            <input type="hidden" name="genre" value="<%= genre %>">

            <select name="sort" id="sortSelect">
                <option value="none" <%= "none".equals(selectedSort) ? "selected" : "" %>>-- price --</option>
                <option value="price_asc" <%= "price_asc".equals(selectedSort) ? "selected" : "" %>>Low -> High</option>
                <option value="price_desc" <%= "price_desc".equals(selectedSort) ? "selected" : "" %>>High -> Low</option>
            </select>
            <button type="submit">Sort</button>
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
