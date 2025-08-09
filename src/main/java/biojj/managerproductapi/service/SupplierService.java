package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.dto.SupplierDTO;
import biojj.managerproductapi.domain.mapper.SupplierMapper;
import biojj.managerproductapi.domain.model.Supplier;
import biojj.managerproductapi.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierDTO save(SupplierDTO dto) {
        Supplier supplier = supplierMapper.toEntity(dto);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    public SupplierDTO getById(Long id) {
        return supplierRepository.findById(id)
                .map(supplierMapper::toDTO)
                .orElse(null);
    }

    public List<SupplierDTO> getAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        supplierRepository.deleteById(id);
    }

    public SupplierDTO update(Long id, SupplierDTO dto) {
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    Supplier supplier = supplierMapper.toEntity(dto);
                    supplier.setId(id);
                    Supplier updatedSupplier = supplierRepository.save(supplier);
                    return supplierMapper.toDTO(updatedSupplier);
                })
                .orElse(null);
    }
}