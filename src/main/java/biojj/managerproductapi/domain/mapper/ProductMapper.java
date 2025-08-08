package biojj.managerproductapi.domain.mapper;

import biojj.managerproductapi.domain.dto.ProductDTO;
import biojj.managerproductapi.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                SupplierMapper.class
        }
)
public interface ProductMapper {
    @Mapping(target = "supplier", ignore = true)
    Product toEntity(ProductDTO dto);

    @Mapping(source = "supplier.id", target = "supplierId")
    ProductDTO toDTO(Product entity);
}