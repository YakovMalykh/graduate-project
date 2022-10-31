package ru.skypro.homework.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    private Long id;
  //  @ManyToOne
  //  @JoinColumn(name = "user_id")
    @Column(name = "author_id")
    private Long author;

    @Column(name = "ads_image")
    private String image;
    @Column(name = "ads_title")
    private String title;
    @Column(name = "ads_description")
    private String description;
    @Column(name = "ads_price")
    private Double price;

 //   @JsonIgnore
   // @OneToMany(mappedBy = "comments")
   // private Collection<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(id, ads.id) && Objects.equals(author, ads.author) && Objects.equals(image, ads.image) && Objects.equals(title, ads.title) && Objects.equals(description, ads.description) && Objects.equals(price, ads.price) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, image, title, description, price);
    }
}
