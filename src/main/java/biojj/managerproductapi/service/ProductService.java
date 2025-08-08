package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.dto.ProductDTO;
import biojj.managerproductapi.domain.mapper.ProductMapper;
import biojj.managerproductapi.domain.model.Product;
import biojj.managerproductapi.domain.model.Supplier;
import biojj.managerproductapi.exception.ObjectNotFoundException;
import biojj.managerproductapi.repository.ProductRepository;
import biojj.managerproductapi.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductDTO save(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ObjectNotFoundException("Fornecedor não encontrado"));

        product.setSupplier(supplier);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElse(null);
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    Product product = productMapper.toEntity(dto);

                    Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                            .orElseThrow(() -> new ObjectNotFoundException("Fornecedor não encontrado"));

                    product.setId(id);
                    product.setSupplier(supplier);
                    Product updatedProduct = productRepository.save(product);
                    return productMapper.toDTO(updatedProduct);
                })
                .orElse(null);
    }
}