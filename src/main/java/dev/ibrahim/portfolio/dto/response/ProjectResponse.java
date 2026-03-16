package dev.ibrahim.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

// ─── Project Response ─────────────────────────────────────────────
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {
    private Long id;
    private String title;
    private String slug;
    private String tagline;
    private String category;
    private String status;
    private String version;
    private String problem;
    private String solution;
    private String scalingStrategy;
    private List<String> techStack;
    private List<String> challenges;
    private List<String> lessonsLearned;
    private String githubUrl;
    private String liveUrl;
    private boolean featured;
    private Instant createdAt;
}
