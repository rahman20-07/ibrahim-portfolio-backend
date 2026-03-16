package dev.ibrahim.portfolio.service;

import dev.ibrahim.portfolio.dto.response.BlogPostResponse;
import dev.ibrahim.portfolio.exception.ResourceNotFoundException;
import dev.ibrahim.portfolio.model.BlogPost;
import dev.ibrahim.portfolio.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    // ── Get all published posts ───────────────────────────────────
    public List<BlogPostResponse> getAllPosts() {
        log.info("Fetching all published blog posts");
        return blogPostRepository.findByPublishedTrueOrderByPublishedAtDesc()
                .stream()
                .map(post -> toResponse(post, false))   // no content in list
                .toList();
    }

    // ── Get latest N posts ────────────────────────────────────────
    public List<BlogPostResponse> getLatestPosts(int limit) {
        log.info("Fetching latest {} blog posts", limit);
        return blogPostRepository
                .findByPublishedTrueOrderByPublishedAtDesc(PageRequest.of(0, limit))
                .stream()
                .map(post -> toResponse(post, false))
                .toList();
    }

    // ── Get single post by slug ───────────────────────────────────
    public BlogPostResponse getPostBySlug(String slug) {
        log.info("Fetching blog post with slug: {}", slug);
        BlogPost post = blogPostRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "slug", slug));
        return toResponse(post, true);  // include full content in detail view
    }

    // ── Entity → DTO mapper ───────────────────────────────────────
    private BlogPostResponse toResponse(BlogPost post, boolean includeContent) {
        return BlogPostResponse.builder()
                .id(post.getId())
                .slug(post.getSlug())
                .title(post.getTitle())
                .excerpt(post.getExcerpt())
                .content(includeContent ? post.getContent() : null)
                .tags(post.getTags())
                .readTime(post.getReadTime())
                .publishedAt(post.getPublishedAt())
                .build();
    }
}
