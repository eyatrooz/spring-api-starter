package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CreateUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User toEntity(CreateUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
    // MapStruct doesn't create new user object it just updates the EXISTING user object

}
