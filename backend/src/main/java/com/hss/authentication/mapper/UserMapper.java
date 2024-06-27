package com.hss.authentication.mapper;

import com.hss.authentication.generated.model.UserResponseDTO;
import com.hss.authentication.generated.model.UserResponseDTOList;
import com.hss.authentication.persistence.model.User;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring")
public interface UserMapper {

    UserResponseDTO userToResponseDTO(User user);

    default UserResponseDTOList usersToUserResponseDTOList(List<User> users) {
        return new UserResponseDTOList().users(users.stream().map(this::userToResponseDTO).toList());
    }
}
