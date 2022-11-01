package ru.skypro.homework.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;//поле pk из AdsCommentDto похоже сюда нужно мапить, т.к. мы ПК объявления
    // передаем в пути при создании комментария, зачем это делать, если бы он был в теле (в
    // JSON)

    @Column(name = "author_id")
    private Long author;

    @Column(name = "ads_id")
    private Long adsId;// откуда его брать???

    @Column(name = "comment_created")
    private LocalDateTime createdAt;
    @Column(name = "comment_text")
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
