package com.birzeit.ordermanagementapi.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {

    CUSTOMER(Set.of(ApplicationUserPermission.ORDER_READ,
            ApplicationUserPermission.ORDER_WRITE,
            ApplicationUserPermission.PRODUCT_READ,
            ApplicationUserPermission.STOCK_READ)),

    MANAGER(Set.of(ApplicationUserPermission.CUSTOMER_READ,
            ApplicationUserPermission.CUSTOMER_WRITE,
            ApplicationUserPermission.PRODUCT_READ,
            ApplicationUserPermission.PRODUCT_WRITE,
            ApplicationUserPermission.MANAGER_READ,
            ApplicationUserPermission.MANAGER_WRITE,
            ApplicationUserPermission.STOCK_READ,
            ApplicationUserPermission.STOCK_WRITE,
            ApplicationUserPermission.ORDER_READ));

    private final Set<ApplicationUserPermission> permissions;

    private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}