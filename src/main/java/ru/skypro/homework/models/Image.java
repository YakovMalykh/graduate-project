package ru.skypro.homework.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "file_size")
    private long fileSize;
    @Column(name = "media_type")
    private String mediaType;
    @Lob
    @Column(name = "prewiew")
    private byte[] prewiew;
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private Ads ads;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return fileSize == image.fileSize && Objects.equals(id, image.id) && Objects.equals(filePath, image.filePath) && Objects.equals(mediaType, image.mediaType) && Arrays.equals(prewiew, image.prewiew) && Objects.equals(ads, image.ads);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, fileSize, mediaType, ads);
        result = 31 * result + Arrays.hashCode(prewiew);
        return result;
    }
}
