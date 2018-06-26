package sweeper;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public Game(int cols, int rows, int boombs) {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(boombs);
        flag = new Flag();
    }

    public GameState getState() {
        return state;
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;

    }

    public Box getBox(Coord coord) {
        if (flag.get(coord) == Box.OPENED) {
            return bomb.get(coord);
        } else {
            return flag.get(coord);
        }
    }

    public void pressLeftButton(Coord coord) {
        if (gameOver()) return;
        openBox(coord);
        checkWinner();
    }

    private void checkWinner() {
        if (state == GameState.PLAYED) {
            if (flag.getCountOfCloseBoxes() == bomb.gettotalBombs()) {
                state = GameState.WINNER;
            }
        }
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED:
                setOpendToCloseBoxesAroundNumber(coord);
                return;
            case FLAGED:
                return;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case ZERO:
                        openBoxesAround(coord);
                        return;
                    case BOMB:
                        openBombs(coord);
                        return;
                    default:
                        flag.setOpenedToBox(coord);
                        return;
                }
        }
    }

    private void openBombs(Coord coord) {
        state = GameState.BOMBED;
        flag.setBombedToBox(coord);
        for (Coord c : Ranges.getAllCords()) {
            if (bomb.get(c) == Box.BOMB) {
                flag.setOpentoCloseBombBox(c);
            } else {
                flag.setNoBombToFlagSafeBox(c);
            }
        }
    }

    private void openBoxesAround(Coord coord) {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordAround(coord)) {
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if (gameOver()) return;
        flag.toggleFlagetToBox(coord);
    }

    private boolean gameOver() {
        if (state == GameState.PLAYED)
            return false;
        start();
        return true;
    }

    void setOpendToCloseBoxesAroundNumber(Coord coord) {
        if (bomb.get(coord) != Box.BOMB) {
            if (flag.getCountOfClosedBoxesAround(coord) == bomb.get(coord).geNumber()) {
                for (Coord around : Ranges.getCoordAround(coord)) {
                    if (flag.get(around) == Box.CLOSED) {
                        openBox(around);
                    }
                }
            }
        }
    }

}
