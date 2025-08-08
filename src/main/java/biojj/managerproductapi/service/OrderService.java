package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.dto.OrderDTO;
import biojj.managerproductapi.domain.dto.OrderItemDTO;
import biojj.managerproductapi.domain.mapper.OrderMapper;
import biojj.managerproductapi.domain.model.*;
import biojj.managerproductapi.exception.ObjectNotFoundException;
import biojj.managerproductapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDTO save(OrderDTO dto) {
        Order order = new Order();

        Buyer buyer = buyerRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new ObjectNotFoundException("Comprador não encontrado"));

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ObjectNotFoundException("Fornecedor não encontrado"));

        order.setBuyer(buyer);
        order.setSupplier(supplier);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        for (OrderItemDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            orderItemRepository.save(item);

            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        savedOrder.setTotalAmount(totalAmount);
        return orderMapper.toDTO(orderRepository.save(savedOrder));
    }

    public OrderDTO getById(Long id) {
        return orderRepository.findByIdWithItems(id)
                .map(order -> {
                    // Debug: Verifique se os itens estão sendo carregados
                    System.out.println("Número de itens carregados: " +
                            (order.getItems() != null ? order.getItems().size() : 0));

                    OrderDTO dto = new OrderDTO();
                    dto.setId(order.getId());
                    dto.setBuyerId(order.getBuyer().getId());
                    dto.setBuyerName(order.getBuyer().getName());
                    dto.setSupplierId(order.getSupplier().getId());
                    dto.setSupplierName(order.getSupplier().getName());
                    dto.setOrderDate(order.getOrderDate());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setStatus(order.getStatus().name());

                    // Mapeamento manual dos itens
                    if (order.getItems() != null) {
                        dto.setItems(order.getItems().stream()
                                .map(item -> {
                                    OrderItemDTO itemDto = new OrderItemDTO();
                                    itemDto.setProductId(item.getProduct().getId());
                                    itemDto.setProductName(item.getProduct().getName());
                                    itemDto.setQuantity(item.getQuantity());
                                    itemDto.setUnitPrice(item.getUnitPrice());
                                    itemDto.setTotalPrice(item.getTotalPrice());
                                    return itemDto;
                                })
                                .collect(Collectors.toList()));
                    } else {
                        dto.setItems(Collections.emptyList());
                    }

                    return dto;
                })
                .orElse(null);
    }

    public List<OrderDTO> getAll() {
        return orderRepository.findAllWithBuyerAndSupplier().stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDTO(order);
                    dto.setBuyerName(order.getBuyer().getName());
                    dto.setSupplierName(order.getSupplier().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<OrderItemDTO> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado"));

        return orderItemRepository.findByOrder(order).stream()
                .map(item -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setProductId(item.getProduct().getId());
                    dto.setProductName(item.getProduct().getName());
                    dto.setQuantity(item.getQuantity());
                    dto.setUnitPrice(item.getUnitPrice());
                    dto.setTotalPrice(item.getTotalPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO updateStatus(Long id, String status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(Order.OrderStatus.valueOf(status));
                    Order updatedOrder = orderRepository.save(order);
                    return orderMapper.toDTO(updatedOrder);
                })
                .orElse(null);
    }
}