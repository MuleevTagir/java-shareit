package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemWithDateMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, CommentRepository commentRepository,
                           BookingRepository bookingRepository, UserRepository userRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User owner = userService.getUserEntityById(userId);

        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Название вещи не может быть пустым");
        }

        ItemRequest itemRequest = null;

        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new IllegalArgumentException("Запрос не найден"));
        }

        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Вещь не найдена"));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NoSuchElementException("Вещь не найдена");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemWithDateDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Вещь не найдена"));
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastBooking = getLastBooking(itemId, now);
        LocalDateTime nextBooking = getNextBooking(itemId, now);

        return ItemWithDateMapper.toDtoWithDate(item, lastBooking, nextBooking);
    }

    @Override
    public List<ItemWithDateDto> getItemsByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    LocalDateTime lastBooking = getLastBooking(item.getId(), now);
                    LocalDateTime nextBooking = getNextBooking(item.getId(), now);

                    return ItemWithDateMapper.toDtoWithDate(item, lastBooking, nextBooking);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) throws ItemNotAvailableException {
        User author = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("Вещь не найдена"));

        boolean hasBooked = bookingRepository.existsByBooker_IdAndItem_IdAndEndIsBefore(userId, itemId,
                LocalDateTime.now());

        if (!hasBooked) {
            throw new ItemNotAvailableException("Пользователь не арендовал вещь или срок аренды не закончился");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);
        comment.setCreated(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(savedComment);
    }

    private LocalDateTime getLastBooking(Long itemId, LocalDateTime now) {
        return bookingRepository.findLastBooking(itemId, now).stream()
                .findFirst()
                .map(booking -> booking.getStart())
                .orElse(null);
    }

    private LocalDateTime getNextBooking(Long itemId, LocalDateTime now) {
        return bookingRepository.findNextBooking(itemId, now).stream()
                .findFirst()
                .map(booking -> booking.getStart())
                .orElse(null);
    }
}