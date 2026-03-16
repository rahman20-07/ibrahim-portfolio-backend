package dev.ibrahim.portfolio.controller;

import dev.ibrahim.portfolio.dto.response.ApiResponse;
import dev.ibrahim.portfolio.dto.response.BlogPostResponse;
import dev.ibrahim.portfolio.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    /**
     * GET /api/blogs
     * Returns all published blog posts (no content field — list view)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getAllPosts() {
        List<BlogPostResponse> posts = blogService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.ok(posts));
    }

    /**
     * GET /api/blogs/latest?limit=3
     * Returns latest N blog posts (for home page highlights)
     */
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getLatestPosts(
            @RequestParam(defaultValue = "3") int limit) {
        List<BlogPostResponse> posts = blogService.getLatestPosts(limit);
        return ResponseEntity.ok(ApiResponse.ok(posts));
    }

    /**
     * GET /api/blogs/{slug}
     * Returns a single post with full content
     * Example: /api/blogs/distributed-url-shortener
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BlogPostResponse>> getPostBySlug(
            @PathVariable String slug) {
        BlogPostResponse post = blogService.getPostBySlug(slug);
        return ResponseEntity.ok(ApiResponse.ok(post));
    }
}
