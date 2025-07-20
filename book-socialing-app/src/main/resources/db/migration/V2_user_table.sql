CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,

    email VARCHAR(255),
    nickname VARCHAR(100),
    role VARCHAR(50),

    created_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255),

    UNIQUE KEY uq_provider_providerId (provider, provider_id),
    INDEX idx_users_created_at (created_at),
    INDEX idx_users_modified_at (modified_at)
);
