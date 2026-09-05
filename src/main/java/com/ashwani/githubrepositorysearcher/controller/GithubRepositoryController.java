package com.ashwani.githubrepositorysearcher.controller;

import com.ashwani.githubrepositorysearcher.dto.request.GithubSearchRequest;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoriesResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubSearchResponse;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.service.GithubRepositoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GithubRepositoryController {

    private final GithubRepositoryService githubRepositoryService;

    public GithubRepositoryController(
            GithubRepositoryService githubRepositoryService) {
        this.githubRepositoryService = githubRepositoryService;
    }

    @PostMapping("/search")
    public ResponseEntity<GithubSearchResponse> searchRepositories(
            @Valid @RequestBody GithubSearchRequest request) {

        GithubSearchResponse response =
                githubRepositoryService.searchRepositories(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/repositories")
    public ResponseEntity<GithubRepositoriesResponse> getStoredRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false) RepositorySortType sort) {

        GithubRepositoriesResponse response =
                githubRepositoryService.getStoredRepositories(
                        language,
                        minStars,
                        sort
                );

        return ResponseEntity.ok(response);
    }
}