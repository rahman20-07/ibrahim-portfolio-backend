package dev.ibrahim.portfolio.service;

import dev.ibrahim.portfolio.dto.response.ProjectResponse;
import dev.ibrahim.portfolio.exception.ResourceNotFoundException;
import dev.ibrahim.portfolio.model.Project;
import dev.ibrahim.portfolio.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    // ── Get all projects ──────────────────────────────────────────
    public List<ProjectResponse> getAllProjects() {
        log.info("Fetching all projects");
        return projectRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Get featured projects ─────────────────────────────────────
    public List<ProjectResponse> getFeaturedProjects() {
        log.info("Fetching featured projects");
        return projectRepository.findByFeaturedTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Get single project by slug ────────────────────────────────
    public ProjectResponse getProjectBySlug(String slug) {
        log.info("Fetching project with slug: {}", slug);
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "slug", slug));
        return toResponse(project);
    }

    // ── Get projects by category ──────────────────────────────────
    public List<ProjectResponse> getProjectsByCategory(String category) {
        log.info("Fetching projects by category: {}", category);
        return projectRepository.findByCategoryOrderByCreatedAtDesc(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Entity → DTO mapper ───────────────────────────────────────
    private ProjectResponse toResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .tagline(p.getTagline())
                .category(p.getCategory())
                .status(p.getStatus())
                .version(p.getVersion())
                .problem(p.getProblem())
                .solution(p.getSolution())
                .scalingStrategy(p.getScalingStrategy())
                .techStack(p.getTechStack())
                .challenges(p.getChallenges())
                .lessonsLearned(p.getLessonsLearned())
                .githubUrl(p.getGithubUrl())
                .liveUrl(p.getLiveUrl())
                .featured(p.isFeatured())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
