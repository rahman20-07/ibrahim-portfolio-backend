package dev.ibrahim.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(nullable = false, length = 500)
    private String tagline;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 50)
    private String status;  // live | in-progress | archived

    @Column(nullable = false, length = 20)
    private String version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String problem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String solution;

    @Column(name = "scaling_strategy", columnDefinition = "TEXT")
    private String scalingStrategy;

    @Column(name = "tech_stack", columnDefinition = "TEXT[]")
    @org.hibernate.annotations.Array(length = 20)
    private List<String> techStack;

    @Column(columnDefinition = "TEXT[]")
    @org.hibernate.annotations.Array(length = 10)
    private List<String> challenges;

    @Column(name = "lessons_learned", columnDefinition = "TEXT[]")
    @org.hibernate.annotations.Array(length = 10)
    private List<String> lessonsLearned;

    @Column(name = "github_url", nullable = false, length = 500)
    private String githubUrl;

    @Column(name = "live_url", length = 500)
    private String liveUrl;

    @Column(nullable = false)
    private boolean featured;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
