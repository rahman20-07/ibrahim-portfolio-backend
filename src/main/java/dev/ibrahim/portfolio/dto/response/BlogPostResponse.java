package dev.ibrahim.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostResponse {
    private Long id;
    private String slug;
    private String title;
    private String excerpt;
    private String content;         // null in list view, populated in detail view
    private List<String> tags;
    private int readTime;
    private Instant publishedAt;
}
