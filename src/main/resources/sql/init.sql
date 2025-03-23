CREATE TABLE IF NOT EXISTS t_Player (
    id uuid PRIMARY KEY ,
    name varchar(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS t_Group (
    id SERIAL PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE,
    prefix varchar(32),
    priority INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS t_PlayerGroupAssignment (
    player_id uuid NOT NULL,
    group_id INT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    PRIMARY KEY (player_id, group_id),
    FOREIGN KEY (player_id) REFERENCES t_Player(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES t_Group(id) ON DELETE CASCADE
);