package com.ashwani.githubrepositorysearcher.dto.external;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class GithubRepositoryItem {
    private Long id;

    private String name;

    private String description;

    private String language;

    @JsonProperty ("stargazers_count")
    private Integer stargazersCount;

    @JsonProperty("forks_count")
    private Integer forksCount;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    private GithubOwner owner;

    public GithubRepositoryItem() {

    }

    public GithubRepositoryItem(Long id, String name, String description, String language, Integer stargazersCount, Integer forksCount, Instant updatedAt, GithubOwner owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.forksCount = forksCount;
        this.updatedAt = updatedAt;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public Integer getForksCount() {
        return forksCount;
    }

    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GithubOwner getOwner() {
        return owner;
    }

    public void setOwner(GithubOwner owner) {
        this.owner = owner;
    }
}
