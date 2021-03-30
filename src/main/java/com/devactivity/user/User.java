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
    private String name;
    private String email;
    private String avatarUrl;
    private String htmlUrl;
    private String blogUrl;
    private Integer publicRepos;
    private Integer publicGists;
    private Integer followers;
    private Integer following;



    private String rssUrl;

    @Builder
    public User(String name, String email, String avatarUrl, String htmlUrl, String blogUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following, String rssUrl) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.blogUrl = blogUrl;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.rssUrl = rssUrl;
    }

    public User update(String name, String email, String avatar_url, String htmlUrl, String blogUrl, Integer publicRepos, Integer publicGists, Integer followers, Integer following) {
        this.name = name;
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
