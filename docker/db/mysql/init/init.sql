USE chess;

CREATE TABLE ChessGame
(
    state varchar(12)
);

create table Piece
(
    file    char(1),
    `rank`  int,
    type    varchar(10),
    color   varchar(5)
);
