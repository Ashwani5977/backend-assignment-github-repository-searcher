package com.ashwani.githubrepositorysearcher.dto.external;

import java.util.List;

public class GithubApiResponse {

    private List<GithubRepositoryItem> items;

    public GithubApiResponse() {

    }

    public GithubApiResponse(List<GithubRepositoryItem> items) {
        this.items = items;
    }

    public List<GithubRepositoryItem> getItems() {
        return items;
    }

    public void setItems(List<GithubRepositoryItem> items) {
        this.items = items;
    }
}