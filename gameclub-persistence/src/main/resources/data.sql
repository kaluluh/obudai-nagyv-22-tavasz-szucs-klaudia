INSERT INTO GAMES (id, name, description, minimum_age, playtime_min, playtime_max, playernum_min, playernum_max) VALUES
( 1, 'Catan Telepesei', 'A Catan telepesei a legtöbb társasjáték rajongónak az első lépés ami túlmutat a gyermekkori klasszikus dobok és lépek játékokon.' ||
                        ' A játék célja Catan szigetén megszerezni az uralmat…', 10, 60, 120, 3, 4),
( 2, 'Pandemic', 'Megvan benned a képesség és a bátorság ahhoz, hogy megmentsd az emberiséget ? Az izgalmas stratégiai játékban egy járványelhárító csapat szakképzett tagjaként feladatod, ' ||
                 'hogy felfedezd a halálos járvány ellenszérumát, még mielőtt az világszerte elterjedne…', 8, 45, 60, 2, 4);

INSERT INTO GAME_CATEGORIES VALUES ( 1, 3 ), (2, 3);

INSERT INTO PLAYERS(id, login_name, name, password, email) VALUES
                ( 1, 'nagys', 'Nagy Sándor', 'ns-secret', 'nagy.sandor@gmail.com'), --superuser
                ( 2, 'horvatha', 'Horváth Ádám', 'ha-secret', 'horvath.adam@gmail.com'), --group-admin
                ( 3, 'kovacsp', 'Kovács Péter', 'kp-secret', 'kovacs.peter@gmail.com'), --player
                ( 4, 'kissi', 'Kiss István', 'ki-secret', 'kiss.istvan@gmail.com'),
                ( 5, 'kaluluh', 'Szücs Klaudia', 'asd-123', 'szucs.klaudia@gmail.com'); --group-admin

INSERT INTO PLAYERS_GAMES VALUES ( 2, 1), ( 2, 2), ( 3, 1 ), (4,2), (5,1);

INSERT INTO PLAYER_ROLES VALUES ( 1, 2 ), ( 2, 1), ( 2, 0), ( 3, 0), ( 4, 0), ( 5, 1),(5,0);

INSERT INTO GROUPS (id, name, admin_id) VALUES ( 1, 'Óbudai Informatika Játék Csoport', 2 );
INSERT INTO GROUPS (id, name, admin_id) VALUES ( 2, 'Kiralyok klubja', 5 );

INSERT INTO GROUPS_MEMBERS VALUES ( 1, 3 ), (2, 1), (2, 2);

INSERT INTO JOIN_REQUESTS VALUES (1, 4, 1), ( 2, 4, 0), ( 2, 3, 0);

INSERT INTO EVENTS (id,date,place,description) VALUES
    ( 1, '2022-11-15 15:30:14.332', 'Board Game Cafe, Ferenc körút 17.','Kata birthday party + play Catan'),
    ( 2, '2022-05-19 16:30', 'At Józsi Apartman, Liszt F. u 17.','Kata birthday party + play Catan');

INSERT INTO EVENTS_PARTICIPANTS VALUES ( 1, 2 ), ( 1, 3), (2, 4);

INSERT INTO GROUPS_EVENTS VALUES ( 1, 1 ), ( 2, 2);
