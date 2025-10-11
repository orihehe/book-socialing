DROP TABLE IF EXISTS chat_rooms;
CREATE TABLE chat_rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    note_id BIGINT NOT NULL,
    room_name VARCHAR(255) NOT NULL,
    state VARCHAR(255),

    deleted TINYINT NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    modified_by VARCHAR(255) NOT NULL,

    INDEX idx_chat_rooms_state (state)
) COMMENT = '채팅방 정보';

DROP TABLE IF EXISTS chat_room_participants;
CREATE TABLE chat_room_participants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    last_read_message_id BIGINT COMMENT '마지막으로 읽은 메시지 ID (읽음 처리용)',

    deleted TINYINT NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    modified_by VARCHAR(255) NOT NULL,

    UNIQUE KEY uq_participant_room_user (room_id, user_id),
    INDEX idx_participants_user_id (user_id),
    INDEX idx_participants_room_id (room_id)
) COMMENT = '채팅방 참여자 목록';

DROP TABLE IF EXISTS chat_messages;
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    message_type VARCHAR(20) NOT NULL DEFAULT 'GENERAL',

    deleted TINYINT NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    modified_by VARCHAR(255) NOT NULL,

    INDEX idx_messages_room_created_at (room_id, created_at DESC)
) COMMENT = '채팅 메시지';
