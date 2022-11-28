package ru.skypro.homework.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL", name = "avatar_id")
    private Long avatarId;
    private String mediaType;
    @Lob
    private byte[] prewiew;
    @OneToOne
    @JoinColumn(name = "author_id")
    private User user;
}