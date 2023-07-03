package com.birzeit.ordermanagementapi.security;

public enum ApplicationUserPermission {

    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write"),
    MANAGER_READ("manager:read"),
    MANAGER_WRITE("manager:write"),
    ORDER_READ("order:read"),
    ORDER_WRITE("order:write"),
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    STOCK_READ("stock:read"),
    STOCK_WRITE("stock:write");

    private final String permission;

    private ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
