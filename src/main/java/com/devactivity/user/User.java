package com.devactivity.user;

import com.devactivity.feed.Feed;
import com.devactivity.repo.Repo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 프로필의 이름
     */
    private String name;

    /**
     * 깃헙 username
     */
    private String login;
    private String email;
    private String avatarUrl;
    private String htmlUrl;
    private String blogUrl;
    /**
     * repository api url
     */
    private String reposUrl;
    private Integer publicRepos;
    private Integer publicGists;
    private Integer followers;
    private Integer following;
    private Integer starCount;
    /**
     * 자기소개
     */
    private String bio;
    private String rssUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "author")
    private Set<Feed> feeds = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Repo> repos = new HashSet<>();

    @Builder
    public User(String name, String login, String email, String avatarUrl, String htmlUrl, String blogUrl, String reposUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following, Integer starCount, String rssUrl, String bio) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.blogUrl = blogUrl;
        this.reposUrl = reposUrl;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.starCount = starCount;
        this.rssUrl = rssUrl;
        this.bio = bio;
    }

    public User update(String name, String userName, String email, String avatar_url, String htmlUrl, String blogUrl, String reposUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following, Integer starCount) {
        this.name = name;
        this.login = userName;
        this.email = email;
        this.avatarUrl = avatar_url;
        this.htmlUrl = htmlUrl;
        this.blogUrl = blogUrl;
        this.reposUrl = reposUrl;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.starCount = starCount;
        return this;
    }
}
