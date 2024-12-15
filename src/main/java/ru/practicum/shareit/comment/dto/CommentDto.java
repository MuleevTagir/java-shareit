package ru.practicum.shareit.comment.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentDto {
    private Long id;
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(Long id, String text, Long itemId, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorName = authorName;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(itemId, that.itemId) && Objects.equals(authorName, that.authorName) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, itemId, authorName, created);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", itemId=" + itemId +
                ", authorName='" + authorName + '\'' +
                ", created=" + created +
                '}';
    }
}