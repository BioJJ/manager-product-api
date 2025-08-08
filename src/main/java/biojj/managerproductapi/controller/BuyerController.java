package biojj.managerproductapi.controller;

import biojj.managerproductapi.domain.dto.BuyerDTO;
import biojj.managerproductapi.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerService buyerService;

    @PostMapping
    public ResponseEntity<BuyerDTO> create(@RequestBody BuyerDTO dto) {
        BuyerDTO createdBuyer = buyerService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuyer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerDTO> getById(@PathVariable Long id) {
        BuyerDTO buyer = buyerService.getById(id);
        return buyer != null ? ResponseEntity.ok(buyer) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BuyerDTO>> getAll() {
        List<BuyerDTO> buyers = buyerService.getAll();
        return ResponseEntity.ok(buyers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuyerDTO> update(@PathVariable Long id, @RequestBody BuyerDTO dto) {
        BuyerDTO updatedBuyer = buyerService.update(id, dto);
        return updatedBuyer != null ? ResponseEntity.ok(updatedBuyer) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        buyerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}