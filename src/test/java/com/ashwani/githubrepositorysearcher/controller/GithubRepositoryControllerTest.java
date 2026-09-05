package com.ashwani.githubrepositorysearcher.controller;

import com.ashwani.githubrepositorysearcher.config.RepositorySortTypeConverter;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoriesResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubRepositoryResponse;
import com.ashwani.githubrepositorysearcher.dto.response.GithubSearchResponse;
import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import com.ashwani.githubrepositorysearcher.exception.GlobalExceptionHandler;
import com.ashwani.githubrepositorysearcher.service.GithubRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubRepositoryController.class)
@Import({
        GlobalExceptionHandler.class,
        RepositorySortTypeConverter.class
})
class GithubRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GithubRepositoryService githubRepositoryService;

    @Test
    void shouldSearchRepositoriesSuccessfully() throws Exception {

        GithubRepositoryResponse repository =
                new GithubRepositoryResponse(
                        6296790L,
                        "spring-boot",
                        "Spring Boot framework",
                        "spring-projects",
                        "Java",
                        81410,
                        42087,
                        Instant.parse("2026-09-05T06:00:36Z")
                );

        GithubSearchResponse response =
                new GithubSearchResponse(
                        "Repositories fetched and saved successfully",
                        List.of(repository)
                );

        when(githubRepositoryService.searchRepositories(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/github/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "query": "spring boot",
                                            "language": "Java",
                                            "sort": "stars"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("Repositories fetched and saved successfully")
                )
                .andExpect(
                        jsonPath("$.repositories[0].id")
                                .value(6296790)
                )
                .andExpect(
                        jsonPath("$.repositories[0].name")
                                .value("spring-boot")
                )
                .andExpect(
                        jsonPath("$.repositories[0].owner")
                                .value("spring-projects")
                )
                .andExpect(
                        jsonPath("$.repositories[0].language")
                                .value("Java")
                )
                .andExpect(
                        jsonPath("$.repositories[0].stars")
                                .value(81410)
                );
    }

    @Test
    void shouldRejectBlankQuery() throws Exception {

        mockMvc.perform(
                        post("/api/github/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "query": "",
                                            "language": "Java",
                                            "sort": "stars"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Validation Failed")
                )
                .andExpect(
                        jsonPath("$.errors.query")
                                .value("Query must not be blank")
                );
    }

    @Test
    void shouldRejectInvalidSortInPostRequest() throws Exception {

        mockMvc.perform(
                        post("/api/github/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "query": "spring boot",
                                            "language": "Java",
                                            "sort": "wrong"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid request body. Please check the provided values."
                                )
                );
    }

    @Test
    void shouldGetStoredRepositoriesSuccessfully() throws Exception {

        GithubRepositoryResponse repository =
                new GithubRepositoryResponse(
                        6296790L,
                        "spring-boot",
                        "Spring Boot framework",
                        "spring-projects",
                        "Java",
                        81410,
                        42087,
                        Instant.parse("2026-09-05T06:00:36Z")
                );

        GithubRepositoriesResponse response =
                new GithubRepositoriesResponse(
                        List.of(repository)
                );

        when(
                githubRepositoryService.getStoredRepositories(
                        eq("Java"),
                        eq(100),
                        eq(RepositorySortType.STARS)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get("/api/github/repositories")
                                .param("language", "Java")
                                .param("minStars", "100")
                                .param("sort", "stars")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.repositories[0].id")
                                .value(6296790)
                )
                .andExpect(
                        jsonPath("$.repositories[0].name")
                                .value("spring-boot")
                )
                .andExpect(
                        jsonPath("$.repositories[0].language")
                                .value("Java")
                )
                .andExpect(
                        jsonPath("$.repositories[0].stars")
                                .value(81410)
                );
    }

    @Test
    void shouldRejectInvalidSortInGetRequest() throws Exception {

        mockMvc.perform(
                        get("/api/github/repositories")
                                .param("sort", "wrong")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid value for request parameter: sort"
                                )
                );
    }
}