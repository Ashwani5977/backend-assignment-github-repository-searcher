package com.ashwani.githubrepositorysearcher.service;

import com.ashwani.githubrepositorysearcher.client.GithubApiClient;
import com.ashwani.githubrepositorysearcher.dto.external.GithubApiResponse;
import com.ashwani.githubrepositorysearcher.dto.external.GithubRepositoryItem;
import com.ashwani.githubrepositorysearcher.dto.external.GithubOwner;
import com.ashwani.githubrepositorysearcher.dto.request.GithubSearchRequest;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoriesResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoryResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubSearchResponse;
import com.ashwani.githubrepositorysearcher.entity.GithubRepositoryEntity;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.repository.GithubRepoRepository;
import com.ashwani.githubrepositorysearcher.service.impl.GithubRepositoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceImplTest {

    @Mock
    private GithubRepoRepository githubRepoRepository;

    @Mock
    private GithubApiClient githubApiClient;

    @InjectMocks
    private GithubRepositoryServiceImpl githubRepositoryService;

    @Test
    void shouldSearchRepositoriesAndSaveResults() {

        GithubSearchRequest request =
                new GithubSearchRequest(
                        "spring boot",
                        "Java",
                        RepositorySortType.STARS
                );

        GithubOwner owner = new GithubOwner();
        owner.setLogin("spring-projects");

        GithubRepositoryItem item = new GithubRepositoryItem();
        item.setId(6296790L);
        item.setName("spring-boot");
        item.setDescription("Spring Boot framework");
        item.setOwner(owner);
        item.setLanguage("Java");
        item.setStargazersCount(81410);
        item.setForksCount(42087);
        item.setUpdatedAt(Instant.parse("2026-09-05T06:00:36Z"));

        GithubApiResponse apiResponse = new GithubApiResponse();
        apiResponse.setItems(List.of(item));

        GithubRepositoryEntity savedEntity =
                new GithubRepositoryEntity(
                        6296790L,
                        "spring-boot",
                        "Spring Boot framework",
                        "spring-projects",
                        "Java",
                        81410,
                        42087,
                        Instant.parse("2026-09-05T06:00:36Z")
                );

        when(githubApiClient.searchRepositories(
                "spring boot",
                "Java",
                RepositorySortType.STARS
        )).thenReturn(apiResponse);

        when(githubRepoRepository.saveAll(any()))
                .thenReturn(List.of(savedEntity));

        GithubSearchResponse response =
                githubRepositoryService.searchRepositories(request);

        assertNotNull(response);
        assertEquals(
                "Repositories fetched and saved successfully",
                response.getMessage()
        );

        assertEquals(1, response.getRepositories().size());

        GithubRepositoryResponse repository =
                response.getRepositories().get(0);

        assertEquals(6296790L, repository.getId());
        assertEquals("spring-boot", repository.getName());
        assertEquals("spring-projects", repository.getOwner());
        assertEquals("Java", repository.getLanguage());
        assertEquals(81410, repository.getStars());
        assertEquals(42087, repository.getForks());

        verify(githubApiClient).searchRepositories(
                "spring boot",
                "Java",
                RepositorySortType.STARS
        );

        verify(githubRepoRepository).saveAll(any());
    }

    @Test
    void shouldReturnRepositoriesSortedByStarsByDefault() {

        GithubRepositoryEntity firstRepository =
                new GithubRepositoryEntity();
        firstRepository.setId(1L);
        firstRepository.setName("repo-one");
        firstRepository.setStars(500);

        GithubRepositoryEntity secondRepository =
                new GithubRepositoryEntity();
        secondRepository.setId(2L);
        secondRepository.setName("repo-two");
        secondRepository.setStars(200);

        List<GithubRepositoryEntity> repositories =
                List.of(firstRepository, secondRepository);

        when(githubRepoRepository.findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "stars")
        )).thenReturn(repositories);

        GithubRepositoriesResponse response =
                githubRepositoryService.getStoredRepositories(
                        null,
                        null,
                        null
                );

        assertNotNull(response);
        assertEquals(2, response.getRepositories().size());
        assertEquals(
                500,
                response.getRepositories()
                        .get(0)
                        .getStars()
        );
        assertEquals(
                200,
                response.getRepositories()
                        .get(1)
                        .getStars()
        );

        verify(githubRepoRepository).findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "stars")
        );
    }

    @Test
    void shouldFilterRepositoriesByLanguageAndMinimumStars() {

        GithubRepositoryEntity repository =
                new GithubRepositoryEntity();

        repository.setId(1L);
        repository.setName("spring-boot");
        repository.setLanguage("Java");
        repository.setStars(500);

        when(githubRepoRepository.findWithFilters(
                "Java",
                100,
                Sort.by(Sort.Direction.DESC, "stars")
        )).thenReturn(List.of(repository));

        GithubRepositoriesResponse response =
                githubRepositoryService.getStoredRepositories(
                        "Java",
                        100,
                        RepositorySortType.STARS
                );

        assertNotNull(response);
        assertEquals(1, response.getRepositories().size());
        assertEquals(
                "Java",
                response.getRepositories()
                        .get(0)
                        .getLanguage()
        );
        assertEquals(
                500,
                response.getRepositories()
                        .get(0)
                        .getStars()
        );

        verify(githubRepoRepository).findWithFilters(
                "Java",
                100,
                Sort.by(Sort.Direction.DESC, "stars")
        );
    }

    @Test
    void shouldSortRepositoriesByForks() {

        when(githubRepoRepository.findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "forks")
        )).thenReturn(List.of());

        GithubRepositoriesResponse response =
                githubRepositoryService.getStoredRepositories(
                        null,
                        null,
                        RepositorySortType.FORKS
                );

        assertNotNull(response);

        verify(githubRepoRepository).findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "forks")
        );
    }

    @Test
    void shouldSortRepositoriesByLastUpdated() {

        when(githubRepoRepository.findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "lastUpdated")
        )).thenReturn(List.of());

        GithubRepositoriesResponse response =
                githubRepositoryService.getStoredRepositories(
                        null,
                        null,
                        RepositorySortType.LAST_UPDATED
                );

        assertNotNull(response);

        verify(githubRepoRepository).findWithFilters(
                null,
                null,
                Sort.by(Sort.Direction.DESC, "lastUpdated")
        );
    }
}