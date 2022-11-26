package ru.skypro.homework.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long avatar_id;
    private  String filePath;
    private  long fileSize;
    private  String mediaType;
    @Lob
    private byte[] prewiew;
    @OneToOne
    @JoinColumn(name = "author_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return fileSize == avatar.fileSize && Objects.equals(avatar_id, avatar.avatar_id) && Objects.equals(filePath, avatar.filePath) && Objects.equals(mediaType, avatar.mediaType) && Arrays.equals(prewiew, avatar.prewiew) && Objects.equals(user, avatar.user);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(avatar_id, filePath, fileSize, mediaType, user);
        result = 31 * result + Arrays.hashCode(prewiew);
        return result;
    }
}
