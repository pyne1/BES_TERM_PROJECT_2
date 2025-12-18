<%@ page import="java.util.*, model.Product, model.Customer, model.Order" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial; background:#f9f9f9; padding:20px; }
        h1 { color:#cc0000; }
        table { border-collapse: collapse; width: 100%; margin-top:20px; }
        th, td { padding:10px; border:1px solid #ddd; text-align:left; }
        th { background:#eee; }
        .nav a { margin-right:20px; font-size:18px; }
        .btn { padding:6px 12px; background:#cc0000; color:white; border:none; cursor:pointer; }
    </style>
</head>

<body>
<%@ include file="logout.jsp" %>


<h1>Admin Dashboard</h1>

<a href="catalog" style="display:inline-block; margin-bottom:15px; font-size:18px; text-decoration:none;"> Back to Catalog </a>


<div class="nav">
    <a href="admin?section=inventory">Inventory</a>
    <a href="admin?section=sales">Sales History</a>
    <a href="admin?section=users">Customers</a>
</div>

<%
String section = (String) request.getAttribute("section");
if (section == null) section = "inventory";
%>

<!-- ===================== INVENTORY ===================== -->
<% if ("inventory".equals(section)) { 
    List<Product> products = (List<Product>) request.getAttribute("products");
%>
<h2>Inventory Management</h2>

<table>
    <tr>
        <th>ID</th><th>Name</th><th>Brand</th><th>Category</th>
        <th>Price</th><th>Quantity</th><th>Action</th>
    </tr>

    <% if (products != null) {
        for (Product p : products) { %>
        <tr>
            <td><%= p.getItemID() %></td>
            <td><%= p.getName() %></td>
            <td><%= p.getBrand() %></td>
            <td><%= p.getCategory() %></td>
            <td>$<%= p.getPrice() %></td>
            <td><%= p.getQuantity() %></td>
            <td>
                <!-- Update Product Form -->
                <form action="admin" method="post">
                    <input type="hidden" name="action" value="updateProduct">
                    <input type="hidden" name="itemID" value="<%= p.getItemID() %>">
                    <input type="text" name="name" value="<%= p.getName() %>" size="12">
                    <input type="text" name="brand" value="<%= p.getBrand() %>" size="6">
                    <input type="text" name="category" value="<%= p.getCategory() %>" size="6">
                    <input type="number" step="0.01" name="price" value="<%= p.getPrice() %>" size="5">
                    <input type="number" name="quantity" value="<%= p.getQuantity() %>" size="5">
                    
                    <!-- button to save -->>
                    <button class="btn">Save</button>
                </form>
            </td>
        </tr>
    <% }} %>
</table>

<!-- Add New Product -->
<h3>Add Product</h3>
<form method="post" action="admin">
    <input type="hidden" name="action" value="addProduct">
    Name: <input type="text" name="name"> 
    Brand: <input type="text" name="brand"> 
    Category: <input type="text" name="category">
    Price: <input type="number" step="0.01" name="price">
    Qty: <input type="number" name="quantity">
    <button class="btn">Add</button>
</form>

<% } %>


<!-- ===================== SALES HISTORY ===================== -->
<% if ("sales".equals(section)) { 
    List<Order> orders = (List<Order>) request.getAttribute("orders"); %>

<h2>Sales History</h2>

<table>
    <tr><th>ID</th><th>Email</th><th>Total</th><th>Date</th></tr>

    <% if (orders != null) {
        for (Order o : orders) { %>
        <tr>
            <td><%= o.getId() %></td>
            <td><%= o.getCustomerEmail() %></td>
            <td>$<%= o.getTotal() %></td>
            <td><%= o.getOrderDate() %></td>
        </tr>
    <% }} %>
</table>

<% } %>


<!-- ===================== USERS ===================== -->
<% if ("users".equals(section)) { 
    List<Customer> customers = (List<Customer>) request.getAttribute("customers"); %>

<h2>Customer List</h2>

<table>
    <tr><th>ID</th><th>Name</th><th>Email</th><th>Action</th></tr>

    <% if (customers != null) {
        for (Customer c : customers) { %>
        <tr>
            <td><%= c.getId() %></td>
            <td><%= c.getFirstName() %> <%= c.getLastName() %></td>
            <td><%= c.getEmail() %></td>

            <td>
                <form method="post" action="admin">
                    <input type="hidden" name="action" value="updateUser">
                    <input type="hidden" name="id" value="<%= c.getId() %>">
                    <input type="text" name="firstName" value="<%= c.getFirstName() %>">
                    <input type="text" name="lastName" value="<%= c.getLastName() %>">
                    <input type="email" name="email" value="<%= c.getEmail() %>">
                    <button class="btn">Save</button>
                </form>
            </td>
        </tr>
    <% }} %>
</table>

<% } %>

</body>
</html>
