package biojj.managerproductapi.domain.mapper;

import biojj.managerproductapi.domain.dto.BuyerDTO;
import biojj.managerproductapi.domain.model.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BuyerMapper {
    Buyer toEntity(BuyerDTO dto);

    BuyerDTO toDTO(Buyer entity);
}