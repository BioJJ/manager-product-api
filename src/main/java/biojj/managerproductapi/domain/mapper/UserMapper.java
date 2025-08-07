package biojj.managerproductapi.domain.mapper;

import biojj.managerproductapi.domain.dto.UserDTO;
import biojj.managerproductapi.domain.model.User;
import biojj.managerproductapi.domain.enums.Profile;
import org.mapstruct.*;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", qualifiedByName = "profileToCode")
    User toEntity(UserDTO dto);

    @Mapping(target = "profiles", qualifiedByName = "codeToProfile")
    UserDTO toDTO(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", qualifiedByName = "profileToCode")
    void updateFromDto(UserDTO dto, @MappingTarget User entity);

    @Named("profileToCode")
    default Set<Integer> profileToCode(Set<Profile> profiles) {
        if (profiles == null) {
            return Collections.emptySet();
        }
        return profiles.stream()
                .map(Profile::getCode)
                .collect(Collectors.toSet());
    }

    @Named("codeToProfile")
    default Set<Profile> codeToProfile(Set<Integer> codes) {
        if (codes == null) {
            return Collections.emptySet();
        }
        return codes.stream()
                .map(Profile::fromCode)
                .collect(Collectors.toSet());
    }
}