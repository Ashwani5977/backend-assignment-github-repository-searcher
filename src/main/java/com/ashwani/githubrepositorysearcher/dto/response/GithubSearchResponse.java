package com.ashwani.githubrepositorysearcher.dto.response;

import java.util.List;

public class GithubSearchResponse {

    private String message;
    private List<GithubRepositoryResponse> repositories;

    public GithubSearchResponse() {

    }

    public GithubSearchResponse(String message, List<GithubRepositoryResponse> repositories) {
        this.message = message;
        this.repositories = repositories;
    }

    public List<GithubRepositoryResponse> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<GithubRepositoryResponse> repositories) {
        this.repositories = repositories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
