USE chess;

CREATE TABLE ChessGame
(
    state varchar(12) check (state in ('init', 'white', 'black', 'terminated'))
);

create table Piece
(
    file    char(1)       check (file between 'A' and 'H'),
    `rank`  int           check (`rank` between 1 and 8),
    type    varchar(10)   check (type in ('initpawn', 'movedpawn', 'rook', 'knight', 'bishop', 'queen', 'king')),
    color   varchar(5)    check (color in ('white', 'black'))
);
