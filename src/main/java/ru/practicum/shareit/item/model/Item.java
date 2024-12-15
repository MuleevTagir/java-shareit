package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Поле available обязательно")
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CommentDto> comments;

    public Item(Long id, String name, String description, Boolean available, User owner, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(available, item.available) && Objects.equals(owner, item.owner) && Objects.equals(comments, item.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, owner, comments);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", owner=" + owner +
                ", comments=" + comments +
                '}';
    }
}