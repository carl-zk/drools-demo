package com.domain.mapstruct;

import com.domain.dto.UserDTO;
import com.domain.entity.BaseEntity;
import com.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * @author carl
 */
@Mapper(imports = {BaseEntity.class})
public interface UserStructMapper {

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOs(List<User> users);

    User toUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO, @MappingTarget User user);
}
