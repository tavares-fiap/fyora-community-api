ALTER TABLE community_users
    ADD COLUMN user_account_id BIGINT UNIQUE;

ALTER TABLE community_users
    ADD CONSTRAINT fk_community_user_account
        FOREIGN KEY (user_account_id) REFERENCES user_accounts(id) ON DELETE SET NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_community_users_user_account_id
    ON community_users(user_account_id);
