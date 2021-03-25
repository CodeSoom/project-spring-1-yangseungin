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

    private String rssUrl;

    @Builder
    public User(String name, String email, String avatarUrl, String rssUrl) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.rssUrl = rssUrl;
    }
}
