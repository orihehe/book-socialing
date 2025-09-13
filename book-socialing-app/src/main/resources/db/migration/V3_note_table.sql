-- Drop if exists
DROP TABLE IF EXISTS note_file;
DROP TABLE IF EXISTS note_participant;
DROP TABLE IF EXISTS note;

-- 1. note 테이블 생성
CREATE TABLE note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- club_id BIGINT, -- TODO: club 구현 후 FK 제약 추가

    book_name VARCHAR(200) NOT NULL,
    book_author VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- TODO: club 테이블과 관계 맺을 경우 (나중에 추가)
    -- 외래 키 제약 조건
    -- CONSTRAINT fk_note_club
    --     FOREIGN KEY (club_id) REFERENCES club(id),

    -- 인덱스
    INDEX idx_note_created_at (created_at),
    INDEX idx_note_modified_at (modified_at)
);

-- 2. note_file 테이블 생성
CREATE TABLE note_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    note_id BIGINT NOT NULL, -- Note 테이블의 ID를 참조
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL UNIQUE, -- 서버에 저장될 고유한 파일명
    file_path VARCHAR(500) NOT NULL, -- 파일 저장 경로 또는 URL
    file_size BIGINT NOT NULL, -- 파일 크기 (바이트 단위)

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 외래 키 제약 조건
    CONSTRAINT fk_note_file_note
        FOREIGN KEY (note_id) REFERENCES note(id),

    -- 인덱스
    INDEX idx_note_file_note_id (note_id),
    INDEX idx_note_file_created_at (created_at),
    INDEX idx_note_file_modified_at (modified_at)
);


-- 3. note_participant 테이블 생성
CREATE TABLE note_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    note_id BIGINT NOT NULL, -- Note 테이블의 ID를 참조
    user_id BIGINT NOT NULL, -- 사용자 테이블의 ID를 참조 (실제 User 테이블과 FK 맺는 것을 고려)

    role VARCHAR(20) NOT NULL, -- 참여자 역할 (HOST, MEMBER)
    status VARCHAR(20) NOT NULL, -- 참여자 상태 (JOINED, PENDING_APPROVAL, CANCEL, REJECTED, LEFT, KICKED)

    created_at DATETIME NOT NULL,
    created_by BIGINT,
    modified_at DATETIME NOT NULL,
    modified_by BIGINT,

    -- 외래 키 제약 조건
    CONSTRAINT fk_note_participant_note
        FOREIGN KEY (note_id) REFERENCES note(id),

    -- 인덱스
    INDEX idx_note_participant_note_id (note_id),
    INDEX idx_note_participant_user_id (user_id),
    INDEX idx_note_participant_created_at (created_at),
    INDEX idx_note_participant_modified_at (modified_at)
);
