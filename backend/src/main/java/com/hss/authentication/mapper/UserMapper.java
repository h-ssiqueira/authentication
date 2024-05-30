package com.hss.authentication.mapper;

import com.hss.authentication.generated.model.UserResponseDTO;
import com.hss.authentication.generated.model.UserResponseDTOList;
import com.hss.authentication.persistence.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;
import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(unmappedTargetPolicy = ERROR, componentModel = "spring")
public interface UserMapper {

    UserResponseDTO UserToResponseDTO(User user);

    default UserResponseDTOList UsersToUserResponseDTOList(List<User> users) {
        return new UserResponseDTOList().users(users.stream().map(this::UserToResponseDTO).toList());
    }
}
