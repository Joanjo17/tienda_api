package com.joanlica.TiendaAPI.auth.service.implementation;

import com.joanlica.TiendaAPI.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.TiendaAPI.auth.model.Role;
import com.joanlica.TiendaAPI.auth.model.UserAuth;
import com.joanlica.TiendaAPI.auth.repository.RoleRepository;
import com.joanlica.TiendaAPI.auth.repository.UserAuthRepository;
import com.joanlica.TiendaAPI.auth.service.UserAuthService;
import com.joanlica.TiendaAPI.client.model.Cliente;
import com.joanlica.TiendaAPI.client.repository.ClienteRepository;
import com.joanlica.TiendaAPI.config.util.JwtUtils;
import com.joanlica.TiendaAPI.core.exception.ClientAlreadyExistsException;
import com.joanlica.TiendaAPI.core.exception.RoleNotFoundException;
import com.joanlica.TiendaAPI.core.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public UserAuthResponseDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        // Verificar si el username ya está en uso. Se lanza excepción si es así.
        if (userAuthRepository.existsByUsername(registerUserRequestDTO.username())) {
            throw new UserAlreadyExistsException("Username is already in use");
        }

        // Mapear DTO a entidad, encriptar la contraseña y guardar el usuario
        var roles = new HashSet<>(roleRepository.findAllById(registerUserRequestDTO.roleIds()));
        if (roles.size() != registerUserRequestDTO.roleIds().size())
            throw new RoleNotFoundException("Some roles not found");

        //Comprobamos que no exista un cliente ya creado con el mismo DNI
        if (clienteRepository.countPorDni(registerUserRequestDTO.dni()) > 0)
            throw new ClientAlreadyExistsException("El cliente ya existe");

        // Guardamos el UserAuth
        var u = new UserAuth();
        u.setUsername(registerUserRequestDTO.username());
        u.setPassword(passwordEncoder.encode(registerUserRequestDTO.password()));
        u.setRolesList(roles);
        UserAuth savedUser = userAuthRepository.save(u);

        // Creación del Cliente asociado al nuevo usuario
        var cliente = new Cliente();
        cliente.setNombre(registerUserRequestDTO.nombre());
        cliente.setApellido(registerUserRequestDTO.apellido());
        cliente.setDni(registerUserRequestDTO.dni());
        cliente.setUser(savedUser);

        clienteRepository.save(cliente);

        // Autenticar al usuario recién registrado y generar el token JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        savedUser.getUsername(),
                        registerUserRequestDTO.password()
                )
        );
        // Se guarda el usuario autenticado en el contexto de seguridad.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);

        // Retornar un DTO de tipo Response con el token
        return new UserAuthResponseDTO(
                savedUser.getId(),
                token,
                savedUser.getRolesList().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public UserAuthResponseDTO login(LoginUserRequestDTO loginUserRequestDTO) {
        // Autenticar al usuario recién registrado y generar el token JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequestDTO.username(),
                        loginUserRequestDTO.password()
                )
        );
        // Se guarda el usuario autenticado en el contexto de seguridad.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);

        //Si ha llegado hasta aquí, es que el usuario existe.
        UserAuth user = userAuthRepository.findByUsername(loginUserRequestDTO.username()).get();

        Set<String> rolesList = user.getRolesList()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        // Retornar un DTO de tipo Response con el token
        return new UserAuthResponseDTO(
                user.getId(),
                token,
                rolesList
        );
    }
}