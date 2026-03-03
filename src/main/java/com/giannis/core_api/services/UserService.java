package com.giannis.core_api.services;

import com.giannis.core_api.dto.UserRequestDto;
import com.giannis.core_api.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    
    UserResponseDto createUser(UserRequestDto userDto);
    
    UserResponseDto getUserById(Long id);
    
    UserResponseDto getUserByEmail(String email);
    
    List<UserResponseDto> getAllUsers();
    
    UserResponseDto updateUser(Long id, UserRequestDto userDto);
    
    void deleteUser(Long id);
}