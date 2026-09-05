package com.ashwani.githubrepositorysearcher.dto.request;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import jakarta.validation.constraints.NotBlank;

public class GithubSearchRequest {

    @NotBlank(message = "Query must not be blank")
    private String query;

    private String language;

    private RepositorySortType sort;

    public GithubSearchRequest() {

    }

    public GithubSearchRequest(String query, String language, RepositorySortType sort) {
        this.query = query;
        this.language = language;
        this.sort = sort;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public RepositorySortType getSort() {
        return sort;
    }

    public void setSort(RepositorySortType sort) {
        this.sort = sort;
    }
}
