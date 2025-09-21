CREATE TABLE community_users (
    id BIGSERIAL PRIMARY KEY,
    community_name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT NOT NULL
);

CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    community_user_id BIGINT NOT NULL,
    supports_count INTEGER DEFAULT 0 NOT NULL,
    FOREIGN KEY (community_user_id) REFERENCES community_users(id) ON DELETE CASCADE
);

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE supports (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP ZONE NOT NULL,
    post_id BIGINT NOT NULL,
    community_user_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (community_user_id) REFERENCES community_users(id) ON DELETE CASCADE,
    UNIQUE (post_id, community_user_id) -- Garante que um usuario só pode apoiar um post uma vez
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    community_user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (community_user_id) REFERENCES community_users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);