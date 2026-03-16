# Portfolio API — Spring Boot Backend

Production-grade REST API for Ibrahim Rahman's engineering portfolio.
Built with Java 17, Spring Boot 3, PostgreSQL, and Flyway.

---

## Architecture

```
Request → CorsFilter → Controller → Service → Repository → PostgreSQL
                           ↓
                   GlobalExceptionHandler
```

## Package Structure

```
src/main/java/dev/ibrahim/portfolio/
│
├── PortfolioApplication.java       ← Entry point
│
├── controller/
│   ├── ProjectController.java      ← GET /api/projects/**
│   ├── BlogController.java         ← GET /api/blogs/**
│   └── ContactController.java      ← POST /api/contact
│
├── service/
│   ├── ProjectService.java         ← Business logic for projects
│   ├── BlogService.java            ← Business logic for blog posts
│   └── ContactService.java         ← Save message + send email
│
├── repository/
│   ├── ProjectRepository.java      ← JPA queries for projects
│   ├── BlogPostRepository.java     ← JPA queries for blogs
│   └── ContactMessageRepository.java
│
├── model/
│   ├── Project.java                ← JPA entity → projects table
│   ├── BlogPost.java               ← JPA entity → blog_posts table
│   └── ContactMessage.java         ← JPA entity → contact_messages table
│
├── dto/
│   ├── request/
│   │   └── ContactRequest.java     ← Validated request body
│   └── response/
│       ├── ApiResponse.java        ← Generic wrapper {success, message, data}
│       ├── ProjectResponse.java    ← Project DTO (never expose entity directly)
│       └── BlogPostResponse.java   ← Blog DTO
│
├── exception/
│   ├── ResourceNotFoundException.java   ← 404 error
│   └── GlobalExceptionHandler.java      ← Centralised error responses
│
└── config/
    ├── CorsConfig.java             ← Allow Next.js frontend origins
    └── JacksonConfig.java          ← JSON serialization settings
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/projects` | All projects |
| GET | `/api/projects/featured` | Featured projects only |
| GET | `/api/projects/{slug}` | Single project by slug |
| GET | `/api/projects/category/{cat}` | Projects by category |
| GET | `/api/blogs` | All published posts (no content) |
| GET | `/api/blogs/latest?limit=3` | Latest N posts |
| GET | `/api/blogs/{slug}` | Single post with full content |
| POST | `/api/contact` | Submit contact form |
| GET | `/actuator/health` | Health check for deployment |

### Example responses

**GET /api/projects**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Distributed URL Shortener",
      "slug": "distributed-url-shortener",
      "category": "distributed-systems",
      "status": "live",
      "techStack": ["Java 17", "Spring Boot", "Redis", "PostgreSQL"]
    }
  ]
}
```

**POST /api/contact** (validation error)
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Please provide a valid email address",
    "message": "Message must be between 10 and 2000 characters"
  }
}
```

---

## Local Setup

### Option A — With Docker (Recommended)

```bash
cd portfolio-backend

# Start PostgreSQL + API together
docker compose up --build

# API runs on: http://localhost:8080
# DB runs on: localhost:5432
```

### Option B — Without Docker (need PostgreSQL installed)

```bash
# 1. Create the database
psql -U postgres -c "CREATE DATABASE portfolio_db;"

# 2. Copy env file and edit
cp .env.example .env

# 3. Run
./mvnw spring-boot:run
```

---

## Database

Schema is managed by **Flyway** — never run raw SQL manually.

| Migration | Description |
|-----------|-------------|
| `V1__init_schema.sql` | Creates all tables + indexes + triggers |
| `V2__seed_data.sql` | Seeds 3 projects + 4 blog posts |

To add a new migration:
```
src/main/resources/db/migration/V3__your_change.sql
```
Flyway auto-runs pending migrations on startup.

---

## Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProjectServiceTest

# With coverage report
./mvnw test jacoco:report
```

Tests use H2 in-memory database — no PostgreSQL needed for tests.

---

## Deploy to Render

1. Push to GitHub
2. Go to [render.com](https://render.com) → New Web Service
3. Connect your repo
4. Settings:
   - **Root Directory**: `backend`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/*.jar`
5. Add environment variables from `.env.example`
6. Deploy ✓

Render auto-deploys on every push to `main`.

---

## Adding a New Project via SQL

```sql
INSERT INTO projects (title, slug, tagline, category, status, version,
                      problem, solution, tech_stack, challenges,
                      lessons_learned, github_url, featured)
VALUES (
    'My New Project',
    'my-new-project',
    'One line description',
    'backend',
    'live',
    'v1.0',
    'What problem does it solve?',
    'How did you solve it?',
    ARRAY['Java', 'Spring Boot'],
    ARRAY['Challenge 1'],
    ARRAY['Lesson 1'],
    'https://github.com/ibrahim/my-new-project',
    false
);
```

---

## Scripts

```bash
./mvnw spring-boot:run          # Run locally
./mvnw test                     # Run tests
./mvnw clean package            # Build JAR
docker compose up --build       # Run with Docker
docker compose down -v          # Stop and remove volumes
```
