package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.enums.Profile;
import biojj.managerproductapi.domain.model.*;
import biojj.managerproductapi.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class DBService {

    @Value("${app.db.initialize:false}")
    private boolean initializeDatabase;

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BCryptPasswordEncoder encoder;

    public DBService(UserRepository userRepository,
                     SupplierRepository supplierRepository,
                     BuyerRepository buyerRepository,
                     ProductRepository productRepository,
                     OrderRepository orderRepository,
                     OrderItemRepository orderItemRepository,
                     BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.buyerRepository = buyerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.encoder = encoder;
    }

    public void instantiateDatabase() {

        if (!initializeDatabase) {
            return;
        }
        log.info("Iniciando inserção de dados iniciais...");

        // Create admin user
        User admin = new User();
        admin.setName("ADM");
        admin.setEmail("adm-manager@gmail.com");
        admin.setPassword(encoder.encode("Admin@2025"));
        admin.setStatus(true);
        admin.setProfiles(new HashSet<>(Arrays.asList(
                Profile.ADMIN.getCode(),
                Profile.USER.getCode()
        )));
        userRepository.save(admin);

        // Create 10 suppliers
        for (int i = 1; i <= 10; i++) {
            Supplier supplier = new Supplier();
            supplier.setName("Fornecedor " + i + " Ltda");
            supplier.setDocument(String.format("%02d.%03d.%03d/0001-%02d", i, i * 100, i * 100, i));
            supplier.setAddress("Rua dos Fornecedores, " + i + "00, Centro - São Paulo/SP");
            supplier.setPhone(String.format("(11) 9%04d-%04d", i * 1000, i * 1000));
            supplier.setEmail("contato@fornecedor" + i + ".com.br");
            supplierRepository.save(supplier);
        }

        // Create 10 buyers
        for (int i = 1; i <= 10; i++) {
            Buyer buyer = new Buyer();
            buyer.setName("Comprador " + i + " da Silva");
            buyer.setDocument(String.format("%03d.%03d.%03d-%02d", i, i * 100, i * 100, i));
            buyer.setAddress("Avenida das Compras, " + i + "00, Bairro Novo - Rio de Janeiro/RJ");
            buyer.setPhone(String.format("(21) 9%04d-%04d", i * 1000, i * 1000));
            buyer.setEmail("comprador" + i + "@email.com");
            buyerRepository.save(buyer);
        }

        // Create 10 products for each supplier
        List<Supplier> suppliers = supplierRepository.findAll();
        for (Supplier supplier : suppliers) {
            for (int i = 1; i <= 10; i++) {
                Product product = new Product();
                product.setName("Produto " + i + " - " + supplier.getName());
                product.setDescription("Descrição do produto " + i + " fornecido por " + supplier.getName());
                product.setSupplier(supplier);
                product.setPrice(BigDecimal.valueOf(1000 * i));
                product.setStockQuantity(100 * i);
                productRepository.save(product);
            }
        }

        // Create some orders
        List<Buyer> buyers = buyerRepository.findAll();
        List<Product> products = productRepository.findAll();

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setBuyer(buyers.get(i % buyers.size()));
            order.setSupplier(suppliers.get(i % suppliers.size()));
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);

            Order savedOrder = orderRepository.save(order);

            // Add 2-4 items to each order
            int itemsCount = 2 + (i % 3);
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (int j = 0; j < itemsCount; j++) {
                Product product = products.get((i + j) % products.size());
                int quantity = 1 + (i % 5);

                OrderItem item = new OrderItem();
                item.setOrder(savedOrder);
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setUnitPrice(product.getPrice());
                item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

                orderItemRepository.save(item);
                totalAmount = totalAmount.add(item.getTotalPrice());
            }

            savedOrder.setTotalAmount(totalAmount);
            orderRepository.save(savedOrder);
        }
        log.info("Dados iniciais inseridos com sucesso!");
    }
}