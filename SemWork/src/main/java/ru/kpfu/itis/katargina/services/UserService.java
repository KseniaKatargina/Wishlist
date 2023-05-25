package ru.kpfu.itis.katargina.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.repositories.UserRepository;
import ru.kpfu.itis.katargina.utils.DtoConverter;
import ru.kpfu.itis.katargina.utils.GetYearsService;
import ru.kpfu.itis.katargina.utils.Role;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserDto saveUser(UserDto userDto) {
        User userForSave;
        try {
            userForSave = userRepository.findByEmail(userDto.getEmail());
            Long id = userForSave.getId();
            userForSave = UserDto.to(userDto);
            userForSave.setId(id);
        } catch (NullPointerException exc){
            userForSave = User.builder()
                    .email(userDto.getEmail())
                    .username(userDto.getUsername())
                    .birthday(userDto.getBirthday())
                    .years(userDto.getYears())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .role(Role.USER)
                    .build();
        }
        User user = userRepository.save(userForSave);
        return UserDto.from(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return DtoConverter.toDto(user, UserDto.class);
    }

    public List<UserDto> findAll(){
        return DtoConverter.toDtoList(userRepository.findAll(), UserDto.class);
    }

    public UserDto findUserByID(Long userID) {
        return  DtoConverter.toDto(userRepository.findUserById(userID), UserDto.class);
    }

    public UserDto findByUsername(String username) {
        return  DtoConverter.toDto(userRepository.findByUsername(username), UserDto.class);
    }
}
