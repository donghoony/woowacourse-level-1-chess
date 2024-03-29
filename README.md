# java-chess

# 기능 요구 사항

### 체스말/기물 (Piece)

- [x] King, Queen, Rook, Knight, Bishop, Pawn 등 여섯 종류의 말이 있다.
- [x] Color와 단위 이동을 통해 움직일 수 있는 최대 횟수를 가진다.
- [x] 출발지와 목적지가 주어졌을 때 이동 가능 여부를 판단한다.
- [x] 폰을 제외한 모든 기물은 기본적으로 이동 조건과 공격 조건이 같다.
- [x] 기물끼리는 특수한 경우를 제외하고 뛰어넘을 수 없다.
- [x] 움직이는 기물과 다른 색을 가진 기물이 목적지에 존재한다면, 기존 기물은 사라진다. (Capture)

### 점수

각 기물은 점수를 가진다.

- [x] 킹(0), 퀸(9), 룩(5), 비숍(3), 나이트(2.5), 폰(1) 점수를 가진다.
- [x] 단, 폰은 같은 세로줄(File)에 위치하는 경우 0.5점을 가진다.

#### 단위 움직임 (UnitMovement)

더 이상 나눌 수 없는 단위 움직임이다.

- [x] 두 점이 주어졌을 때, 단위 움직임을 구한다.
- [x] 행, 열 차가 주어졌을 때, 단위 움직임을 구한다.
- [x] 단위 움직임을 이용해 다음 위치를 구한다.

#### 킹

- [x] 상하좌우, 대각선 한 칸씩 이동할 수 있다.
- [ ] (특수 룰: 캐슬링)

#### 퀸

- [x] 상하좌우, 대각선 원하는 만큼 이동할 수 있다.

#### 룩

- [x] 상하좌우 원하는 만큼 이동할 수 있다.
- [ ] (특수 룰: 캐슬링)

#### 비숍

- [x] 대각선 원하는 만큼 이동할 수 있다.

#### 나이트

- [x] 상하좌우 두 칸 전진한 뒤, 전진한 방향의 90도 좌/우 한 칸으로 이동할 수 있다.
- [x] 나이트는 다른 말을 뛰어넘을 수 있다.

#### 폰

- [x] 앞으로 한 칸 전진할 수 있다.
- [x] 앞쪽 대각선에 상대의 기물이 존재하는 경우, 상대방의 기물을 잡으면서 그 칸으로 이동할 수 있다.
- [x] 한 번도 움직이지 않았다면, 두 칸 전진할 수 있다.
- [x] 후진할 수 없다.
- [ ] (특수 룰: 앙파상, 프로모션)

### 색 (Color)

- [x] WHITE, BLACK 두 가지의 색을 가진다.

### 위치 (Position)

- [x] File, Rank 로 이루어져 있다.

### File (체스 판의 열)

- [x] a ~ h 까지 총 8개의 열이 있다.

### Rank (체스 판의 행)

- [x] 1 ~ 8 까지 총 8개의 행이 있다.

### 경로 (Path)

- [x] 주어진 칸으로 경로를 만든다. (길이는 2 이상이어야 한다.)
- [x] 경로에 기물 존재 여부를 판단한다.

### 점수 (Score)

체스의 점수를 나타낸다. 기본적으로 0.5 단위이며, 그렇지 않은 점수를 생성하는 경우 예외를 발생한다.

- [x] 점수끼리의 덧셈, 곱셈을 수행한다.
- [x] 절반으로 나눈다. 이때, 이미 소수점 단위가 존재한다면 예외를 발생한다.

### 판 (Board)

- [x] Position 과 Piece 를 매핑한다.
- [x] 두 점이 주어지면 기물을 이동한다.
- 아래 조건 중 적어도 하나에 해당되면 예외를 발생한다. 괄호 속 클래스는 예외를 발생하는 것을 책임진다.
    - 출발 칸에 기물이 없는 경우 (Square)
    - 도착 칸에 출발 칸과 같은 색의 기물이 있는 경우 (Square)
    - 경로 사이에 기물이 있는 경우 (Path)
    - 기물이 도착 칸으로 이동할 수 없는 경우 (Square -> Piece)
    - 상대방의 기물을 움직이는 경우 (Path)
- [x] 현재 판의 기물 상태로 점수를 계산한다.

### 칸 (Square)

- [x] 기물을 가진다.
- [x] 목표 칸으로 기물을 옮긴다.
- [x] 가지고 있는 기물이 움직일 수 있는지를 판단한다. (외부에서 방향과 움직임 횟수를 받음)
- [x] 출발 칸에 기물이 있는지, 출발/도착이 서로 같은 색인지를 판단한다.

### 게임 상태

게임 상태는 준비(InitState), 진행(TurnState), 종료(TerminatedState)로 구성돼 있다.

- [x] 준비 상태에서는 움직이거나 종료할 수 없다.
- [x] 진행 상태에서는 시작할 수 없다.
- [x] 종료 상태에서는 시작하거나, 움직이거나, 종료할 수 없다.
- [x] 진행 상태에서는 한 번 턴 움직임을 진행할 때마다 상대방의 상태로 전이된다.
- [x] 킹이 잡히면 게임이 종료된다.

### BoardInitializer

- [x] 최초 판에 말을 놓은 Board 를 만든다.
