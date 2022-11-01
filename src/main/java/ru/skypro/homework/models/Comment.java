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
    private Long id;

    @Column(name = "author_id")//здесь тоже ссылка на User может быть нужна?
    private Long author;

    // вероятно нужно так прописывать здесь поле, так в БД должен сохраниться Id
    // обявления, в объявлении в свою очередь нкжно установить связь @OneToMany
    // на поле List<Comment> comments
//    @ManyToOne
//    @Column(name = "ads_id")
//    private Ads ads;
    @Column(name = "ads_id")
    private Long adsId;

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
