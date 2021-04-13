package com.devactivity.repo;

import com.devactivity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.util.Objects;

@Entity
@Getter
@EqualsAndHashCode(of = "fullName")
@NoArgsConstructor
@AllArgsConstructor
public class Repo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User owner;

    private String name;
    private String fullName;
    private String about;
    private String language;
    private Integer starCount;
    private Integer forkCount;
    private String url;

    @Builder
    public Repo(User owner, String name,String fullName, String about, String language, Integer starCount, Integer forkCount, String url) {
        this.owner = owner;
        this.name = name;
        this.fullName = fullName;
        this.about = about;
        this.language = language;
        this.starCount = starCount;
        this.forkCount = forkCount;
        this.url = url;
    }

    @PrePersist
    public void prePersist() {
        this.about = Objects.isNull(this.about) ? "" : this.about;
        this.language = Objects.isNull(this.language) ? "" : this.language;
    }
}
