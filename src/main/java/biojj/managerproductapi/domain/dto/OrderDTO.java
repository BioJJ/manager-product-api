package biojj.managerproductapi.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long buyerId;
    private String buyerName;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private String status;
}