package com.sape.safety_for_people.config;

import com.sape.safety_for_people.model.Role;
import com.sape.safety_for_people.model.Status;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.data-initializer.enabled", havingValue = "true", matchIfMissing = false)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public void run(String... args) {
        seedStatuses();
        seedRoles();
        seedSampleUsers();
        seedSampleSales();
    }

    private void seedStatuses() {
        if (statusRepository.count() == 0) {
            List<Status> statuses = Arrays.asList(
                crearStatus("Pendiente"),
                crearStatus("Enviado"),
                crearStatus("Entregado"),
                crearStatus("Pagado"),
                crearStatus("Cancelado")
            );
            statusRepository.saveAll(statuses);
            System.out.println("[DataInitializer] Estados iniciales creados: " + statuses.size());
        }
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                crearRole("ROLE_USER"),
                crearRole("ROLE_ADMIN")
            );
            roleRepository.saveAll(roles);
            System.out.println("[DataInitializer] Roles iniciales creados.");
        }
    }

    private void seedSampleUsers() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@safety.com");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setPhoneNumber("3001234567");
            admin.setActive(true);

            User user = new User();
            user.setName("Usuario Demo");
            user.setEmail("user@safety.com");
            user.setPassword(passwordEncoder.encode("User123!"));
            user.setPhoneNumber("3007654321");
            user.setActive(true);

            userRepository.saveAll(Arrays.asList(admin, user));
            System.out.println("[DataInitializer] Usuarios de prueba creados.");
        }
    }

    private void seedSampleSales() {
        if (saleRepository.count() == 0) {
            Status pendiente = statusRepository.findByName("Pendiente").orElse(null);
            Status enviado = statusRepository.findByName("Enviado").orElse(null);
            Status entregado = statusRepository.findByName("Entregado").orElse(null);
            Status pagado = statusRepository.findByName("Pagado").orElse(null);

            if (pendiente == null || enviado == null || entregado == null || pagado == null) {
                System.out.println("[DataInitializer] Faltan estados para ventas de prueba.");
                return;
            }

            List<com.sape.safety_for_people.model.Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                System.out.println("[DataInitializer] No hay productos para ventas de prueba.");
                return;
            }

            User admin = userRepository.findByEmail("admin@safety.com").orElse(null);
            if (admin == null) {
                admin = new User();
                admin.setName("Administrador");
                admin.setEmail("admin@safety.com");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setPhoneNumber("3001234567");
                admin.setActive(true);
                userRepository.save(admin);
                System.out.println("[DataInitializer] Admin creado para ventas.");
            }

            List<com.sape.safety_for_people.model.Sale> sampleSales = Arrays.asList(
                crearVenta(entregado, products.get(0), 2, admin, LocalDateTime.now().minusDays(1)),
                crearVenta(enviado, products.get(1), 1, admin, LocalDateTime.now().minusDays(2)),
                crearVenta(pendiente, products.get(2), 3, admin, LocalDateTime.now().minusDays(3)),
                crearVenta(pagado, products.get(0), 1, admin, LocalDateTime.now().minusDays(4)),
                crearVenta(entregado, products.get(3), 2, admin, LocalDateTime.now().minusDays(5)),
                crearVenta(enviado, products.get(4), 1, admin, LocalDateTime.now().minusDays(6)),
                crearVenta(pendiente, products.get(5), 2, admin, LocalDateTime.now().minusDays(7))
            );

            saleRepository.saveAll(sampleSales);
            System.out.println("[DataInitializer] " + sampleSales.size() + " ventas de prueba creadas.");
        }
    }

    private Status crearStatus(String name) {
        Status s = new Status();
        s.setName(name);
        s.setActive(true);
        return statusRepository.save(s);
    }

    private Role crearRole(String name) {
        Role r = new Role();
        r.setName(name);
        r.setActive(true);
        return roleRepository.save(r);
    }

    private com.sape.safety_for_people.model.Sale crearVenta(Status status, com.sape.safety_for_people.model.Product product, int cantidad, User user, LocalDateTime fecha) {
        com.sape.safety_for_people.model.Sale sale = new com.sape.safety_for_people.model.Sale();
        sale.setStatus(status);
        sale.setQuantity(cantidad);
        sale.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(cantidad)));
        sale.setActive(true);
        sale.setCreatedOn(fecha);
        sale.setUser(user);

        var detail = new com.sape.safety_for_people.model.SaleDetail();
        detail.setPrice(product.getPrice());
        detail.setQuantity(cantidad);
        detail.setSale(sale);
        detail.setProduct(product);
        sale.getDetails().add(detail);

        return sale;
    }
}