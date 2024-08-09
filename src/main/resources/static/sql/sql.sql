CREATE TABLE member (
    member_id VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(100) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    point NUMBER DEFAULT 0
);

alter table member add social NUMBER(1) DEFAULT 0; -- 소셜 로그인 여부 (0: 일반, 1: 소셜)
alter table member add del NUMBER(1) DEFAULT 0; -- 삭제 여부 (0: 사용, 1: 삭제)


CREATE TABLE role (
    role_id NUMBER PRIMARY KEY,
    role_name VARCHAR2(50) UNIQUE NOT NULL
);

CREATE TABLE member_role (
    member_id VARCHAR2(50),
    role_id NUMBER,
    PRIMARY KEY (member_id, role_id),
    CONSTRAINT fk_member_role_member FOREIGN KEY (member_id) REFERENCES member(member_id),
    CONSTRAINT fk_member_role_role FOREIGN KEY (role_id) REFERENCES role(role_id)
);

-- Role 테이블에 권한 추가
INSERT INTO role (role_id, role_name) VALUES (1, 'ADMIN');
INSERT INTO role (role_id, role_name) VALUES (2, 'MANAGER');
INSERT INTO role (role_id, role_name) VALUES (3, 'USER');

-- Member 테이블에 사용자 추가
INSERT INTO member (member_id, password, name, email) VALUES ('java', '1234', '홍길동', 'magic@dream.com');

-- MemberRole 테이블에 사용자 권한 추가
INSERT INTO member_role (member_id, role_id) VALUES ('java', 1);  -- ADMIN
INSERT INTO member_role (member_id, role_id) VALUES ('java', 2);  -- MANAGER
INSERT INTO member_role (member_id, role_id) VALUES ('java', 3);  -- USER

