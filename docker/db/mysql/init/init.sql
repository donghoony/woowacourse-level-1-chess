USE chess;

CREATE TABLE ChessGame
(
    room_name varchar(50) primary key not null,
    state     varchar(12)
);

create table Piece
(
    room_name varchar(50) not null,
    file   char(1),
    `rank` int,
    type   varchar(10),
    color  varchar(5),
    foreign key (room_name) references ChessGame(room_name)
);
