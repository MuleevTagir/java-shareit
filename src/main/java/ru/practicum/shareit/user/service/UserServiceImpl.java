package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public UserDto createUser(UserDto userDto) {
        if (!isEmailUnique(userDto.getEmail())) {
            throw new IllegalArgumentException("Email уже существует");
        }

        User user = UserMapper.toUser(userDto);
        user.setId(idGenerator.getAndIncrement());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }

        if (userDto.getEmail() != null) {
            if (users.values().stream()
                    .anyMatch(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))) {
                throw new IllegalArgumentException("Email уже существует");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        users.put(userId, existingUser);
        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        if (!this.isExist(userId)) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        users.remove(userId);
    }

    private boolean isEmailUnique(String email) {
        return users.values().stream().noneMatch(user -> user.getEmail().equals(email));
    }

    private boolean isExist(Long userId) {
        return users.containsKey(userId);
    }
}