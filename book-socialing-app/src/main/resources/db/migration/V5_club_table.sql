-- Drop if exists
DROP TABLE IF EXISTS club_file;
DROP TABLE IF EXISTS club_participant;
DROP TABLE IF EXISTS club_review;
DROP TABLE IF EXISTS club;

-- 1. club 테이블 생성
CREATE TABLE club (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    club_name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 인덱스
    INDEX idx_club_created_at (created_at),
    INDEX idx_club_modified_at (modified_at)
);

-- 2. club_file 테이블 생성
CREATE TABLE club_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    club_id BIGINT NOT NULL, -- club 테이블의 ID를 참조
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL UNIQUE, -- 서버에 저장될 고유한 파일명
    file_path VARCHAR(500) NOT NULL, -- 파일 저장 경로 또는 URL
    file_size BIGINT NOT NULL, -- 파일 크기 (바이트 단위)

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 외래 키 제약 조건
    CONSTRAINT fk_club_file_club
        FOREIGN KEY (club_id) REFERENCES club(id),

    -- 인덱스
    INDEX idx_club_file_club_id (club_id),
    INDEX idx_club_file_created_at (created_at),
    INDEX idx_club_file_modified_at (modified_at)
);


-- 3. club_participant 테이블 생성
CREATE TABLE club_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    club_id BIGINT NOT NULL, -- club 테이블의 ID를 참조
    user_id BIGINT NOT NULL, -- 사용자 테이블의 ID를 참조 (실제 User 테이블과 FK 맺는 것을 고려)

    role VARCHAR(20) NOT NULL, -- 참여자 역할 (HOST, MEMBER)
    status VARCHAR(20) NOT NULL, -- 참여자 상태 (JOINED, PENDING_APPROVAL, CANCEL, REJECTED, LEFT, KICKED)

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 외래 키 제약 조건
    CONSTRAINT fk_club_participant_club
        FOREIGN KEY (club_id) REFERENCES club(id),

    -- 인덱스
    INDEX idx_club_participant_club_id (club_id),
    INDEX idx_club_participant_user_id (user_id),
    INDEX idx_club_participant_created_at (created_at),
    INDEX idx_club_participant_modified_at (modified_at)
);

-- 4. club_review 테이블 생성
CREATE TABLE club_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    club_id BIGINT NOT NULL, -- 후기를 남길 대상 club의 ID
    user_id BIGINT NOT NULL, -- 후기를 작성한 user의 ID

    rating TINYINT NOT NULL, -- 평점 (예: 1~5점)
    content TEXT, -- 후기 내용

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 외래 키 제약 조건
    CONSTRAINT fk_club_review_club
        FOREIGN KEY (club_id) REFERENCES club(id),
    -- CONSTRAINT fk_club_review_user
    --     FOREIGN KEY (user_id) REFERENCES user(id), -- 실제 User 테이블 생성 후 FK 제약 추가

    -- 유니크 제약 조건: 한 명의 유저는 하나의 클럽에 대해 후기를 한 번만 작성할 수 있도록 설정
    UNIQUE KEY uk_club_review_club_id_user_id (club_id, user_id),

    -- 인덱스
    INDEX idx_club_review_club_id (club_id),
    INDEX idx_club_review_user_id (user_id),
    INDEX idx_club_review_created_at (created_at),
    INDEX idx_club_review_modified_at (modified_at)
);
