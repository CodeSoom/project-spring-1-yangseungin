package com.devactivity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
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
    private Integer publicRepos;
    private Integer publicGists;
    private Integer followers;
    private Integer following;
    /**
     * 자기소개
     */
    private String bio;
    private String rssUrl;

    @Builder
    public User(String name, String login, String email, String avatarUrl, String htmlUrl, String blogUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following, String rssUrl, String bio) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.blogUrl = blogUrl;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.rssUrl = rssUrl;
        this.bio = bio;
    }

    public User update(String name, String userName, String email, String avatar_url, String htmlUrl, String blogUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following) {
        this.name = name;
        this.login = userName;
        this.email = email;
        this.avatarUrl = avatar_url;
        this.htmlUrl = htmlUrl;
        this.blogUrl = blogUrl;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        return this;
    }
}
