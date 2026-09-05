package com.ashwani.githubrepositorysearcher.repository;

import com.ashwani.githubrepositorysearcher.entity.GithubRepositoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GithubRepoRepository extends JpaRepository<GithubRepositoryEntity, Long> {

    @Query ("""
             SELECT r 
             FROM GithubRepositoryEntity r
             WHERE (:language IS NULL OR r.language = :language)
             AND (:minStars IS NULL OR r.stars >= :minStars)
            """)
    List<GithubRepositoryEntity> findWithFilters(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            Sort sort
    );
}
