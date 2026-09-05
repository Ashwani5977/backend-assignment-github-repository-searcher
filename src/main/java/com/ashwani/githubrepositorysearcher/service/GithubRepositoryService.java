package com.ashwani.githubrepositorysearcher.service;

import com.ashwani.githubrepositorysearcher.dto.request.GithubSearchRequest;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoriesResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoryResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubSearchResponse;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;

import java.util.List;

public interface GithubRepositoryService {

    GithubSearchResponse searchRepositories (
            GithubSearchRequest githubSearchRequest
    );

    GithubRepositoriesResponse getStoredRepositories(
            String language,
            Integer minStars,
            RepositorySortType sort);
}
