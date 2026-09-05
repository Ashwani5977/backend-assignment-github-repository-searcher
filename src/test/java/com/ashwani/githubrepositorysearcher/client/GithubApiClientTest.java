package com.ashwani.githubrepositorysearcher.client;

import com.ashwani.githubrepositorysearcher.dto.external.GithubApiResponse;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.exception.GithubApiException;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GithubApiClientTest {

    @Test
    void shouldCreateGithubApiClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();

        GithubApiClient githubApiClient =
                new GithubApiClient(restClient);

        assertNotNull(githubApiClient);
    }

    @Test
    void shouldUseGithubApiException() {

        GithubApiException exception =
                new GithubApiException(
                        "Failed to retrieve repositories from GitHub"
                );

        assertEquals(
                "Failed to retrieve repositories from GitHub",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateGithubApiExceptionWithCause() {

        RuntimeException cause =
                new RuntimeException("API error");

        GithubApiException exception =
                new GithubApiException(
                        "GitHub API request failed",
                        cause
                );

        assertEquals(
                "GitHub API request failed",
                exception.getMessage()
        );

        assertEquals(
                cause,
                exception.getCause()
        );
    }
}