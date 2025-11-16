package com.joanlica.TiendaAPI.auth.service;

import com.joanlica.TiendaAPI.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.UserAuthResponseDTO;

public interface UserAuthService {
    // Metodo para registrar un nuevo usuario
    UserAuthResponseDTO register(RegisterUserRequestDTO registerUserRequestDTO);

    // Metodo para loguear un usuario
    UserAuthResponseDTO login(LoginUserRequestDTO loginUserRequestDTO);
}