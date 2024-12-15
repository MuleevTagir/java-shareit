package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Поле available обязательно")
    private Boolean available;

    private List<CommentDto> comments;

    public ItemDto(Long id, String name, String description, Boolean available, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Название не может быть пустым") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Название не может быть пустым") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Описание не может быть пустым") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Описание не может быть пустым") String description) {
        this.description = description;
    }

    public @NotNull(message = "Поле available обязательно") Boolean getAvailable() {
        return available;
    }

    public void setAvailable(@NotNull(message = "Поле available обязательно") Boolean available) {
        this.available = available;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }



    @Override
    public String toString() {
        return "ItemDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", comments=" + comments +
                '}';
    }
}