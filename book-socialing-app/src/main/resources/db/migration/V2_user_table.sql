DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,

    email VARCHAR(255),
    nickname VARCHAR(100),
    description VARCHAR(1000),
    role VARCHAR(50),

    deleted TINYINT NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255) NOT NULL,

    UNIQUE KEY uq_provider_providerId (provider, provider_id),
    UNIQUE KEY uq_email (email),
    INDEX idx_users_created_at (created_at),
    INDEX idx_users_modified_at (modified_at)
);

-- user_file 테이블 생성
CREATE TABLE user_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, -- User 테이블의 ID를 참조
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL UNIQUE, -- 서버에 저장될 고유한 파일명
    file_path VARCHAR(500) NOT NULL, -- 파일 저장 경로 또는 URL
    file_size BIGINT NOT NULL, -- 파일 크기 (바이트 단위)

    deleted TINYINT NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255),

    -- 외래 키 제약 조건
    CONSTRAINT fk_user_file_note
        FOREIGN KEY (user_id) REFERENCES users(id),

    -- 인덱스
    INDEX idx_user_file_user_id (user_id),
    INDEX idx_user_file_created_at (created_at),
    INDEX idx_user_file_modified_at (modified_at)
);
