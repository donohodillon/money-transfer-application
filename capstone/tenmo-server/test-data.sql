BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer, account, tenmo_user, transfer_type, transfer_status;
DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;


CREATE TABLE transfer_type (
                               transfer_type_id serial NOT NULL,
                               transfer_type_desc varchar(10) NOT NULL,
                               CONSTRAINT PK_transfer_type PRIMARY KEY (transfer_type_id)
);

CREATE TABLE transfer_status (
                                 transfer_status_id serial NOT NULL,
                                 transfer_status_desc varchar(10) NOT NULL,
                                 CONSTRAINT PK_transfer_status PRIMARY KEY (transfer_status_id)
);

CREATE SEQUENCE seq_user_id
    INCREMENT BY 1
    START WITH 1001
    NO MAXVALUE;

CREATE TABLE tenmo_user (
                            user_id int NOT NULL DEFAULT nextval('seq_user_id'),
                            username varchar(50) NOT NULL,
                            CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
                            CONSTRAINT UQ_username UNIQUE (username)
);

CREATE SEQUENCE seq_account_id
    INCREMENT BY 1
    START WITH 2001
    NO MAXVALUE;

CREATE TABLE account (
                         account_id int NOT NULL DEFAULT nextval('seq_account_id'),
                         user_id int NOT NULL,
                         balance decimal(13, 2) NOT NULL,
                         CONSTRAINT PK_account PRIMARY KEY (account_id),
                         CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
    INCREMENT BY 1
    START WITH 3001
    NO MAXVALUE;

CREATE TABLE transfer (
                          transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
                          transfer_type_id int NOT NULL,
                          transfer_status_id int NOT NULL,
                          account_from int NOT NULL,
                          account_to int NOT NULL,
                          amount decimal(13, 2) NOT NULL,
                          CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
                          CONSTRAINT FK_transfer_account_from FOREIGN KEY (account_from) REFERENCES account (account_id),
                          CONSTRAINT FK_transfer_account_to FOREIGN KEY (account_to) REFERENCES account (account_id),
                          CONSTRAINT FK_transfer_transfer_status FOREIGN KEY (transfer_status_id) REFERENCES transfer_status (transfer_status_id),
                          CONSTRAINT FK_transfer_transfer_type FOREIGN KEY (transfer_type_id) REFERENCES transfer_type (transfer_type_id),
                          CONSTRAINT CK_transfer_not_same_account CHECK (account_from <> account_to),
                          CONSTRAINT CK_transfer_amount_gt_0 CHECK (amount > 0)
);


INSERT INTO transfer_status (transfer_status_desc) VALUES ('Pending');
INSERT INTO transfer_status (transfer_status_desc) VALUES ('Approved');
INSERT INTO transfer_status (transfer_status_desc) VALUES ('Rejected');

INSERT INTO transfer_type (transfer_type_desc) VALUES ('Request');
INSERT INTO transfer_type (transfer_type_desc) VALUES ('Send');

INSERT INTO tenmo_user (user_id, username) VALUES (1050, 'John');
INSERT INTO tenmo_user (user_id, username) VALUES (1051, 'Dan');

INSERT INTO account (account_id, user_id, balance) VALUES (2050, 1050, 700);
INSERT INTO account (account_id, user_id, balance) VALUES (2051, 1051, 500);

INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (3050, 2, 2, 2050, 2051, 200);
INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (3051, 2, 2, 2051, 2050, 100);

COMMIT;