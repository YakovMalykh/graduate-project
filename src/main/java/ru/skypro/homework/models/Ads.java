package ru.skypro.homework.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "ads_title")
    private String title;
    @Column(name = "ads_description")
    private String description;
    @Column(name = "ads_price")
    private Integer price;

    @Column(name = "ads_image")
    private String image;

    @OneToMany(mappedBy = "ads")
    @JsonIgnore
    private List<Image> images;
    @JsonIgnore
    @OneToMany(mappedBy = "adsId")
    private Collection<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return author.equals(ads.author) && title.equals(ads.title) && description.equals(ads.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, description);
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Ads ads = (Ads) o;
//        return Objects.equals(id, ads.id) && Objects.equals(author, ads.author) && Objects.equals(images, ads.images) && Objects.equals(title, ads.title) && Objects.equals(description, ads.description) && Objects.equals(price, ads.price) && Objects.equals(comments, ads.comments);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, author, images, title, description, price, comments);
//    }

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                ", comments=" + comments +
                '}';
    }
}
