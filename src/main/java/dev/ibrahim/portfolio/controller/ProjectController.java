package dev.ibrahim.portfolio.controller;

import dev.ibrahim.portfolio.dto.response.ApiResponse;
import dev.ibrahim.portfolio.dto.response.ProjectResponse;
import dev.ibrahim.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * GET /api/projects
     * Returns all projects ordered by creation date (newest first)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.ok(projects));
    }

    /**
     * GET /api/projects/featured
     * Returns featured projects for home page
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getFeaturedProjects() {
        List<ProjectResponse> projects = projectService.getFeaturedProjects();
        return ResponseEntity.ok(ApiResponse.ok(projects));
    }

    /**
     * GET /api/projects/{slug}
     * Returns a single project by slug
     * Example: /api/projects/distributed-url-shortener
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectBySlug(
            @PathVariable String slug) {
        ProjectResponse project = projectService.getProjectBySlug(slug);
        return ResponseEntity.ok(ApiResponse.ok(project));
    }

    /**
     * GET /api/projects/category/{category}
     * Returns projects filtered by category
     * Example: /api/projects/category/distributed-systems
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByCategory(
            @PathVariable String category) {
        List<ProjectResponse> projects = projectService.getProjectsByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok(projects));
    }
}
