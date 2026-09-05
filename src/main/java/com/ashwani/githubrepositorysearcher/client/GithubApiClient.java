package com.ashwani.githubrepositorysearcher.client;

import com.ashwani.githubrepositorysearcher.dto.external.GithubApiResponse;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.exception.GithubApiException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class GithubApiClient {

    private final RestClient restClient;

    public GithubApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public GithubApiResponse searchRepositories(
            String query,
            String language,
            RepositorySortType sort
    ) {

        String queryWithLanguage = query + " in:name";

        if (language != null && !language.isBlank()) {
            queryWithLanguage += " language:" + language;
        }

        final String finalQuery = queryWithLanguage;

        try {

            return restClient.get()
                    .uri(uriBuilder -> {

                        uriBuilder
                                .path("/search/repositories")
                                .queryParam("q", finalQuery);

                        if (sort != null) {

                            String sortValue = sort.getValue();

                            uriBuilder
                                    .queryParam("sort", sortValue)
                                    .queryParam("order", "desc");
                        }

                        return uriBuilder.build();
                    })
                    .retrieve()
                    .body(GithubApiResponse.class);

        } catch (RestClientResponseException exception) {

            HttpStatusCode statusCode = exception.getStatusCode();

            if (statusCode.value() == 403 || statusCode.value() == 429) {
                throw new GithubApiException(
                        "GitHub API rate limit exceeded. Please try again later.",
                        exception
                );
            }

            throw new GithubApiException(
                    "GitHub API request failed with status "
                            + statusCode.value(),
                    exception
            );

        } catch (RestClientException exception) {

            throw new GithubApiException(
                    "Failed to retrieve repositories from GitHub",
                    exception
            );
        }
    }
}