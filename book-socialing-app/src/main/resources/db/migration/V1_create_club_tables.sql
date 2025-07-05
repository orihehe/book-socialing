-- Drop if exists
DROP TABLE IF EXISTS meeting_participant;
DROP TABLE IF EXISTS meeting;

-- meeting 테이블
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    book_name VARCHAR(200),
    book_author VARCHAR(100),
    book_link VARCHAR(200),
    meet_date DATETIME NOT NULL,
    round INT NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255),

    INDEX idx_meeting_created_at (created_at),
    INDEX idx_meeting_modified_at (modified_at)
);

-- meeting_participant 테이블
CREATE TABLE meeting_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255),

    CONSTRAINT fk_meeting_participant_meeting
        FOREIGN KEY (meeting_id) REFERENCES meeting(id),

    -- ✅ 인덱스
    INDEX idx_participant_created_at (created_at),
    INDEX idx_participant_modified_at (modified_at)
);
