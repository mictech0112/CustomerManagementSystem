CREATE TABLE issues (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    summary VARCHAR(256) NOT NULL,
    description VARCHAR(256) NOT NULL,
    liked BOOLEAN NOT NULL DEFAULT FALSE,
    image BYTEA, -- バイナリデータを格納するために型を変更
    deleted_at TIMESTAMP NULL DEFAULT NULL -- 論理削除用のカラムを追加
);

CREATE TABLE users (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL,
    authority VARCHAR(10) NOT NULL CHECK (authority IN ('ADMIN', 'USER')),
    deleted_at TIMESTAMP NULL DEFAULT NULL -- 論理削除用のカラムを追加
);

CREATE TABLE user_issue_likes (
    user_id BIGINT NOT NULL,
    issue_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, issue_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (issue_id) REFERENCES issues(id),
    deleted_at TIMESTAMP NULL DEFAULT NULL -- 論理削除用のカラムを追加
);