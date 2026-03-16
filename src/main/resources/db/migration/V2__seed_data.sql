-- =====================================================================
-- V2__seed_data.sql
-- Sample data — projects and blog posts
-- =====================================================================

INSERT INTO projects (title, slug, tagline, category, status, version,
                      problem, solution, scaling_strategy,
                      tech_stack, challenges, lessons_learned,
                      github_url, live_url, featured)
VALUES
(
    'Distributed URL Shortener',
    'distributed-url-shortener',
    'Horizontally scalable URL shortener handling 10k+ RPS with <10ms p99 latency',
    'distributed-systems', 'live', 'v1.2',
    'Design a system that shortens long URLs and handles millions of redirect requests per day with low latency. A single database becomes a bottleneck at scale.',
    'Consistent hashing for shard routing across DB nodes. Redis as a read-through cache with 24hr TTL. Snowflake-inspired Base62 ID generation with zero coordination between nodes.',
    'Horizontal scaling of API nodes behind Nginx. DB sharding via consistent hashing. Redis cluster for cache. Read replicas for redirect traffic (90% of load).',
    ARRAY['Java 17', 'Spring Boot', 'PostgreSQL', 'Redis', 'Docker', 'Nginx'],
    ARRAY['Hash collision avoidance in ID generation',
          'Cache invalidation on URL updates without stale reads',
          '301 vs 302 redirect trade-off (SEO vs analytics)',
          'Unique ID generation at scale without bottleneck'],
    ARRAY['Reads dominate writes 100:1 — design the read path first',
          'Lua scripts in Redis = atomic operations without locks',
          'Snowflake IDs are better than UUID for distributed systems'],
    'https://github.com/ibrahimrahman/url-shortener',
    'https://demo.ibrahim.dev',
    true
),
(
    'Token Bucket Rate Limiter',
    'rate-limiter',
    'Distributed rate limiter with 3 pluggable algorithms via Spring AOP annotation',
    'rate-limiting', 'live', 'v1.0',
    'APIs need protection from abuse. Need a configurable, distributed rate limiter consistent across multiple Spring Boot instances.',
    'Redis + Lua scripts for atomic token operations. Spring AOP @RateLimit annotation for declarative usage. Supports Token Bucket, Sliding Window, and Fixed Window.',
    'Redis cluster handles state across all service instances. Lua scripts execute atomically — no distributed locks needed.',
    ARRAY['Java', 'Spring Boot', 'Redis', 'Lua', 'Spring AOP'],
    ARRAY['Race conditions in non-atomic check-and-decrement',
          'Clock drift across distributed nodes',
          'Sliding window requires storing per-request timestamps — memory trade-off'],
    ARRAY['Never do check-then-act without atomicity — classic race condition',
          'Token Bucket is best for bursty real-user traffic',
          'AOP makes rate limiting transparent to business logic'],
    'https://github.com/ibrahimrahman/rate-limiter',
    NULL,
    true
),
(
    'Real-time Chat System',
    'realtime-chat',
    'WebSocket-based messaging with Redis pub/sub fan-out across service instances',
    'real-time', 'in-progress', 'v0.3',
    'Build a horizontally scalable real-time chat where messages reach all online users regardless of which server instance they are connected to.',
    'WebSocket for persistent connections. Redis Pub/Sub for cross-instance message fan-out. Kafka for guaranteed delivery and message persistence.',
    'Each server subscribes to user-specific Redis channels. Kafka for durability. Separate read/write models for scaling.',
    ARRAY['Java', 'WebSocket', 'Redis Pub/Sub', 'Kafka', 'Spring Boot'],
    ARRAY['Connection state not shared across instances',
          'Fan-out write for large groups is expensive',
          'Message ordering guarantees in distributed environment'],
    ARRAY['Still learning — in progress'],
    'https://github.com/ibrahimrahman/realtime-chat',
    NULL,
    false
);

INSERT INTO blog_posts (slug, title, excerpt, content, tags, read_time, published, published_at)
VALUES
(
    'distributed-url-shortener',
    'How I Built a Distributed URL Shortener',
    'Walking through the architecture decisions, trade-offs, and implementation of a horizontally scalable URL shortener service.',
    'The URL shortener problem is a classic system design question at FAANG companies — and for good reason. It touches consistent hashing, distributed ID generation, caching strategy, and read/write ratio trade-offs all at once.

## The Core Problem

At low scale, a URL shortener is trivial. The complexity starts when you ask: what happens at 10,000 redirects per second?

## ID Generation

I implemented a Snowflake-inspired approach — 64-bit ID = timestamp (41 bits) + datacenter ID (5 bits) + machine ID (5 bits) + sequence (12 bits). This gives ~4096 unique IDs per millisecond per machine, with zero coordination.

## Cache Strategy

Read-through caching with Redis. Cache miss hits PostgreSQL, populates Redis with 24hr TTL. Write path saves to DB first, then invalidates cache.',
    ARRAY['distributed-systems', 'system-design', 'java', 'redis'],
    8, true, NOW() - INTERVAL '60 days'
),
(
    'load-balancers-explained',
    'Understanding Load Balancers: Round Robin to Consistent Hashing',
    'From L4 to L7, sticky sessions, health checks, and why consistent hashing matters for stateful services.',
    'Load balancers seem simple on the surface — distribute traffic across servers — but hide tremendous complexity when you dig deeper.

## L4 vs L7

L4 load balancers (transport layer) forward TCP/UDP packets without inspecting content. Blazing fast. L7 (application layer) inspect HTTP headers — smart routing at higher CPU cost.

## Consistent Hashing

Standard hash(request) % n_servers breaks when you add/remove servers — nearly all sessions remap. Consistent hashing places both servers and requests on a hash ring. Adding a node remaps only K/n keys. This is why Redis Cluster and DynamoDB use it.',
    ARRAY['system-design', 'load-balancing', 'distributed-systems'],
    6, true, NOW() - INTERVAL '30 days'
),
(
    'rate-limiter-algorithms',
    'Designing a Rate Limiter: 3 Algorithms Compared',
    'Token Bucket vs Sliding Window vs Fixed Window — trade-offs, Redis implementation, and when to use each.',
    'Rate limiting is non-negotiable for production APIs. But which algorithm?

## Token Bucket
Tokens fill at a fixed rate. Requests consume tokens. Allows bursting — users can consume accumulated tokens instantly. Best for real user traffic patterns.

## Fixed Window
Simple counter per time window. Vulnerable at window boundaries — a user can send 2x rate by straddling windows.

## Sliding Window Log
Stores timestamp of each request. Most accurate. Memory-intensive at scale.

## Redis + Lua
Always use Lua scripts for atomic check-and-decrement. Non-atomic = race conditions under concurrent load.',
    ARRAY['system-design', 'rate-limiting', 'redis', 'java'],
    7, true, NOW() - INTERVAL '45 days'
),
(
    'redis-beyond-cache',
    'Redis Explained: Why It''s More Than Just a Cache',
    'Pub/Sub, sorted sets, Lua scripts, persistence modes — the full Redis picture beyond key-value.',
    'Most engineers use Redis as a simple string cache. But Redis is a data structure server.

## Sorted Sets (ZSETs)
O(log N) insertion and range queries. Perfect for leaderboards, priority queues, and sliding window rate limiters.

## Pub/Sub
Fire-and-forget messaging. No persistence, no consumer groups. For real-time notifications where dropping a message is acceptable.

## Lua Scripting
Scripts execute atomically. No other Redis command runs between statements — essential for rate limiters and distributed locks.

## Persistence Modes
RDB: point-in-time snapshots. AOF: log every write command. Most production setups use both.',
    ARRAY['redis', 'distributed-systems', 'backend'],
    5, true, NOW() - INTERVAL '90 days'
);
