<%@ page import="model.Customer" %>

<%
    Customer adminBtnUser = (Customer) session.getAttribute("currentCustomer");

    boolean isAdminUser = (adminBtnUser != null
            && "admin@everything.yorku.ca".equalsIgnoreCase(adminBtnUser.getEmail()));

    if (isAdminUser) {
%>
        <a href="admin?section=inventory" class="btn btn-warning">Admin Dashboard</a>
<%
    }
%>
