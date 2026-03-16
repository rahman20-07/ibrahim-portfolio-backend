package dev.ibrahim.portfolio;

import dev.ibrahim.portfolio.exception.ResourceNotFoundException;
import dev.ibrahim.portfolio.model.Project;
import dev.ibrahim.portfolio.repository.ProjectRepository;
import dev.ibrahim.portfolio.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService Tests")
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = Project.builder()
                .id(1L)
                .title("Distributed URL Shortener")
                .slug("distributed-url-shortener")
                .tagline("Horizontally scalable URL shortener")
                .category("distributed-systems")
                .status("live")
                .version("v1.2")
                .problem("Scale to millions of requests")
                .solution("Consistent hashing + Redis cache")
                .techStack(List.of("Java", "Spring Boot", "Redis"))
                .challenges(List.of("Hash collisions", "Cache invalidation"))
                .lessonsLearned(List.of("Design read path first"))
                .githubUrl("https://github.com/ibrahim/url-shortener")
                .featured(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("getAllProjects() returns list of all projects")
    void getAllProjects_returnsAll() {
        when(projectRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(sampleProject));

        var result = projectService.getAllProjects();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Distributed URL Shortener");
        assertThat(result.get(0).getSlug()).isEqualTo("distributed-url-shortener");
        verify(projectRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("getProjectBySlug() returns correct project when found")
    void getProjectBySlug_found() {
        when(projectRepository.findBySlug("distributed-url-shortener"))
                .thenReturn(Optional.of(sampleProject));

        var result = projectService.getProjectBySlug("distributed-url-shortener");

        assertThat(result.getTitle()).isEqualTo("Distributed URL Shortener");
        assertThat(result.getCategory()).isEqualTo("distributed-systems");
    }

    @Test
    @DisplayName("getProjectBySlug() throws ResourceNotFoundException when not found")
    void getProjectBySlug_notFound() {
        when(projectRepository.findBySlug("nonexistent"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectBySlug("nonexistent"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("nonexistent");
    }

    @Test
    @DisplayName("getFeaturedProjects() returns only featured projects")
    void getFeaturedProjects_returnsOnlyFeatured() {
        when(projectRepository.findByFeaturedTrueOrderByCreatedAtDesc())
                .thenReturn(List.of(sampleProject));

        var result = projectService.getFeaturedProjects();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isFeatured()).isTrue();
    }
}
