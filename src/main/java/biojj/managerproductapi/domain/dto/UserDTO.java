package biojj.managerproductapi.domain.dto;

import biojj.managerproductapi.domain.enums.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Status is required")
    private Boolean status = true;

    private Set<Profile> profiles;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCreation = LocalDate.now();
}