CREATE TABLE roles
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

CREATE TABLE user_roles
(
    role_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id          UUID         NOT NULL,
    username    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    phone_no    VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

INSERT INTO public.roles (id, name) VALUES ('53a61d1a-c28d-4d0a-9217-4693e508457f', 'ROLE_USER');
INSERT INTO public.roles (id, name) VALUES ('5c3dae67-4679-4ab3-8013-6a19861d4082', 'ROLE_ADMIN');

INSERT INTO public.users (id, email, first_name, is_verified, last_name, password, phone_no, username) VALUES ('8eabb4ff-df5b-4e39-8642-0dcce375798c', 'domino222@gmail.com', 'Michael', true, 'Jordan', '$2a$10$PVuuLUBi1u4QH9jDH6dyr.ad9TjupUnYUpVq/XzWiJCJDJbu1Ni4G', '+35984238424', 'domino222');
INSERT INTO public.users (id, email, first_name, is_verified, last_name, password, phone_no, username) VALUES ('1a419320-edf3-4b69-8f41-ce472f866a19', 'domino22@gmail.com', 'Michael', false, 'Jordan', '$2a$10$oULmJMLnSgOkS3N7AuqBv.tvSuXS..qnnymqHzvyP3Qi/4DB9j2fu', '+35984238424323', 'domino22');

INSERT INTO user_roles (role_id, user_id)
VALUES
    ('53a61d1a-c28d-4d0a-9217-4693e508457f', '8eabb4ff-df5b-4e39-8642-0dcce375798c'),
    ('5c3dae67-4679-4ab3-8013-6a19861d4082', '8eabb4ff-df5b-4e39-8642-0dcce375798c'),
    ('53a61d1a-c28d-4d0a-9217-4693e508457f', '1a419320-edf3-4b69-8f41-ce472f866a19');