package com.birzeit.ordermanagementapi.authentication;

import com.birzeit.ordermanagementapi.entities.Customer;
import com.birzeit.ordermanagementapi.entities.Manager;
import com.birzeit.ordermanagementapi.entities.User;
import com.birzeit.ordermanagementapi.security.ApplicationUserRole;
import com.birzeit.ordermanagementapi.services.CustomerService;
import com.birzeit.ordermanagementapi.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository("MySqlDB")
public class DBApplicationUserDaoService implements ApplicationUserDAO {

    private final PasswordEncoder passwordEncoder;

    private final CustomerService customerService;

    private final ManagerService managerService;

    public DBApplicationUserDaoService(
            PasswordEncoder passwordEncoder,
            CustomerService customerService,
            ManagerService managerService) {
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.managerService = managerService;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        List<Customer> customers = customerService.getCustomers();
        List<Manager> managers = managerService.getManagers();
        Set<SimpleGrantedAuthority> grantedAuthorities;

        for(Customer customer: customers) {
            grantedAuthorities = ApplicationUserRole.CUSTOMER.getGrantedAuthorities();
            ApplicationUser applicationUser = new ApplicationUser
                    (customer.getUserCredentials().getUsername(),
                            customer.getUserCredentials().getPassword(),
                            grantedAuthorities,
                            true,
                            true,
                            true,
                            true
                    );
            applicationUsers.add(applicationUser);
        }

        for(Manager manager: managers) {
            grantedAuthorities = ApplicationUserRole.MANAGER.getGrantedAuthorities();
            ApplicationUser applicationUser = new ApplicationUser
                    (manager.getUserCredentials().getUsername(),
                            manager.getUserCredentials().getPassword(),
                            grantedAuthorities,
                            true,
                            true,
                            true,
                            true
                    );
            applicationUsers.add(applicationUser);
        }

        return applicationUsers;
    }
}