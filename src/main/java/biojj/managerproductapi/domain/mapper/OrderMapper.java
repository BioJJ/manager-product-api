package biojj.managerproductapi.domain.mapper;

import biojj.managerproductapi.domain.dto.OrderDTO;
import biojj.managerproductapi.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class}
)
public interface OrderMapper {
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderDTO dto);

    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "supplier.id", target = "supplierId")
    OrderDTO toDTO(Order entity);

    List<OrderDTO> toDTOList(List<Order> entities);
}