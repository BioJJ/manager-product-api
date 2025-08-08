package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.dto.BuyerDTO;
import biojj.managerproductapi.domain.mapper.BuyerMapper;
import biojj.managerproductapi.domain.model.Buyer;
import biojj.managerproductapi.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerService {
    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;

    public BuyerDTO save(BuyerDTO dto) {
        Buyer buyer = buyerMapper.toEntity(dto);
        Buyer savedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toDTO(savedBuyer);
    }

    public BuyerDTO getById(Long id) {
        return buyerRepository.findById(id)
                .map(buyerMapper::toDTO)
                .orElse(null);
    }

    public List<BuyerDTO> getAll() {
        return buyerRepository.findAll().stream()
                .map(buyerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        buyerRepository.deleteById(id);
    }

    public BuyerDTO update(Long id, BuyerDTO dto) {
        return buyerRepository.findById(id)
                .map(existingBuyer -> {
                    Buyer buyer = buyerMapper.toEntity(dto);
                    buyer.setId(id);
                    Buyer updatedBuyer = buyerRepository.save(buyer);
                    return buyerMapper.toDTO(updatedBuyer);
                })
                .orElse(null);
    }
}