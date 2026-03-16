package dev.ibrahim.portfolio.repository;

import dev.ibrahim.portfolio.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findBySlug(String slug);

    List<Project> findByFeaturedTrueOrderByCreatedAtDesc();

    List<Project> findAllByOrderByCreatedAtDesc();

    List<Project> findByCategoryOrderByCreatedAtDesc(String category);

    boolean existsBySlug(String slug);
}
