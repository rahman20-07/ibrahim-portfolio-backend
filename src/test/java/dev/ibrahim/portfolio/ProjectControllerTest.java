package dev.ibrahim.portfolio;

import dev.ibrahim.portfolio.dto.response.ProjectResponse;
import dev.ibrahim.portfolio.exception.ResourceNotFoundException;
import dev.ibrahim.portfolio.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = dev.ibrahim.portfolio.controller.ProjectController.class)
@DisplayName("ProjectController Integration Tests")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    @DisplayName("GET /api/projects returns 200 with project list")
    void getAllProjects_returns200() throws Exception {
        ProjectResponse project = ProjectResponse.builder()
                .id(1L)
                .title("Distributed URL Shortener")
                .slug("distributed-url-shortener")
                .category("distributed-systems")
                .status("live")
                .featured(true)
                .build();

        when(projectService.getAllProjects()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Distributed URL Shortener"))
                .andExpect(jsonPath("$.data[0].slug").value("distributed-url-shortener"));
    }

    @Test
    @DisplayName("GET /api/projects/{slug} returns 404 when project not found")
    void getProjectBySlug_returns404() throws Exception {
        when(projectService.getProjectBySlug("nonexistent"))
                .thenThrow(new ResourceNotFoundException("Project", "slug", "nonexistent"));

        mockMvc.perform(get("/api/projects/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
