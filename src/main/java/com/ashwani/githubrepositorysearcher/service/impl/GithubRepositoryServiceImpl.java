package com.ashwani.githubrepositorysearcher.service.impl;

import com.ashwani.githubrepositorysearcher.client.GithubApiClient;
import com.ashwani.githubrepositorysearcher.dto.external.GithubApiResponse;
import com.ashwani.githubrepositorysearcher.dto.external.GithubRepositoryItem;
import com.ashwani.githubrepositorysearcher.dto.request.GithubSearchRequest;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoriesResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoryResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubSearchResponse;
import com.ashwani.githubrepositorysearcher.entity.GithubRepositoryEntity;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.repository.GithubRepoRepository;
import com.ashwani.githubrepositorysearcher.service.GithubRepositoryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubRepositoryServiceImpl implements GithubRepositoryService {

    private final GithubRepoRepository githubRepoRepository;
    private final GithubApiClient githubApiClient;

    public GithubRepositoryServiceImpl(
            GithubRepoRepository githubRepoRepository,
            GithubApiClient githubApiClient) {
        this.githubRepoRepository = githubRepoRepository;
        this.githubApiClient = githubApiClient;
    }

    @Override
    public GithubSearchResponse searchRepositories(
            GithubSearchRequest githubSearchRequest) {

        GithubApiResponse githubApiResponse =
                githubApiClient.searchRepositories(
                        githubSearchRequest.getQuery(),
                        githubSearchRequest.getLanguage(),
                        githubSearchRequest.getSort()
                );

        List<GithubRepositoryEntity> repositories =
                githubApiResponse.getItems()
                        .stream()
                        .map(this::mapToEntity)
                        .toList();

        List<GithubRepositoryEntity> savedRepositories =
                githubRepoRepository.saveAll(repositories);

        List<GithubRepositoryResponse> repositoryResponses =
                savedRepositories
                        .stream()
                        .map(this::mapToDto)
                        .toList();

        return new GithubSearchResponse(
                "Repositories fetched and saved successfully",
                repositoryResponses
        );
    }

    @Override
    public GithubRepositoriesResponse getStoredRepositories(
            String language,
            Integer minStars,
            RepositorySortType sort) {

        Sort sort1;

        if (sort == null) {
            sort1 = Sort.by(
                    Sort.Direction.DESC,
                    "stars"
            );
        } else {
            sort1 = switch (sort) {
                case STARS ->
                        Sort.by(
                                Sort.Direction.DESC,
                                "stars"
                        );

                case FORKS ->
                        Sort.by(
                                Sort.Direction.DESC,
                                "forks"
                        );

                case LAST_UPDATED ->
                        Sort.by(
                                Sort.Direction.DESC,
                                "lastUpdated"
                        );
            };
        }

        List<GithubRepositoryEntity> repositories =
                githubRepoRepository.findWithFilters(
                        language,
                        minStars,
                        sort1
                );

        List<GithubRepositoryResponse> repositoryResponses =
                repositories
                        .stream()
                        .map(this::mapToDto)
                        .toList();

        return new GithubRepositoriesResponse(repositoryResponses);
    }

    private GithubRepositoryResponse mapToDto(
            GithubRepositoryEntity githubRepository) {

        return new GithubRepositoryResponse(
                githubRepository.getId(),
                githubRepository.getName(),
                githubRepository.getDescription(),
                githubRepository.getOwner(),
                githubRepository.getLanguage(),
                githubRepository.getStars(),
                githubRepository.getForks(),
                githubRepository.getLastUpdated()
        );
    }

    private GithubRepositoryEntity mapToEntity(
            GithubRepositoryItem githubRepositoryItem) {

        GithubRepositoryEntity entity =
                new GithubRepositoryEntity();

        entity.setId(githubRepositoryItem.getId());
        entity.setName(githubRepositoryItem.getName());
        entity.setDescription(githubRepositoryItem.getDescription());

        entity.setOwner(
                githubRepositoryItem.getOwner().getLogin()
        );

        entity.setLanguage(githubRepositoryItem.getLanguage());
        entity.setStars(
                githubRepositoryItem.getStargazersCount()
        );
        entity.setForks(
                githubRepositoryItem.getForksCount()
        );
        entity.setLastUpdated(
                githubRepositoryItem.getUpdatedAt()
        );

        return entity;
    }
}