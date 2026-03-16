package dev.ibrahim.portfolio.repository;

import dev.ibrahim.portfolio.model.BlogPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findBySlugAndPublishedTrue(String slug);

    List<BlogPost> findByPublishedTrueOrderByPublishedAtDesc();

    List<BlogPost> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    boolean existsBySlug(String slug);
}
