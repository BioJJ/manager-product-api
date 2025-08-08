package biojj.managerproductapi.domain.mapper;

import biojj.managerproductapi.domain.dto.SupplierDTO;
import biojj.managerproductapi.domain.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SupplierMapper {
    Supplier toEntity(SupplierDTO dto);

    SupplierDTO toDTO(Supplier entity);
}