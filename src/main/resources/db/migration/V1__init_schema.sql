-- =====================================================================
-- V1__init_schema.sql
-- Portfolio DB — initial schema
-- =====================================================================

-- ── Projects ─────────────────────────────────────────────────────
CREATE TABLE projects (
    id          BIGSERIAL       PRIMARY KEY,
    title       VARCHAR(200)    NOT NULL,
    slug        VARCHAR(200)    NOT NULL UNIQUE,
    tagline     VARCHAR(500)    NOT NULL,
    category    VARCHAR(100)    NOT NULL,
    status      VARCHAR(50)     NOT NULL DEFAULT 'live',
    version     VARCHAR(20)     NOT NULL DEFAULT 'v1.0',
    problem     TEXT            NOT NULL,
    solution    TEXT            NOT NULL,
    scaling_strategy    TEXT,
    tech_stack  TEXT[]          NOT NULL DEFAULT '{}',
    challenges  TEXT[]          NOT NULL DEFAULT '{}',
    lessons_learned TEXT[]      NOT NULL DEFAULT '{}',
    github_url  VARCHAR(500)    NOT NULL,
    live_url    VARCHAR(500),
    featured    BOOLEAN         NOT NULL DEFAULT false,
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_projects_slug     ON projects(slug);
CREATE INDEX idx_projects_featured ON projects(featured);
CREATE INDEX idx_projects_status   ON projects(status);

-- ── Blog Posts ───────────────────────────────────────────────────
CREATE TABLE blog_posts (
    id           BIGSERIAL       PRIMARY KEY,
    slug         VARCHAR(200)    NOT NULL UNIQUE,
    title        VARCHAR(300)    NOT NULL,
    excerpt      TEXT            NOT NULL,
    content      TEXT            NOT NULL,
    tags         TEXT[]          NOT NULL DEFAULT '{}',
    read_time    INT             NOT NULL DEFAULT 5,
    published    BOOLEAN         NOT NULL DEFAULT false,
    published_at TIMESTAMPTZ,
    created_at   TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_blog_slug      ON blog_posts(slug);
CREATE INDEX idx_blog_published ON blog_posts(published);

-- ── Contact Messages ─────────────────────────────────────────────
CREATE TABLE contact_messages (
    id         BIGSERIAL       PRIMARY KEY,
    name       VARCHAR(200)    NOT NULL,
    email      VARCHAR(300)    NOT NULL,
    message    TEXT            NOT NULL,
    ip_address VARCHAR(50),
    read       BOOLEAN         NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

-- ── Auto-update updated_at trigger ───────────────────────────────
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_blog_posts_updated_at
    BEFORE UPDATE ON blog_posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
