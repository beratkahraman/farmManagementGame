public class Player {
    private int x, y;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveUp() {
        if (y > 0) y--;
    }

    public void moveDown(int rows) {
        if (y < rows - 1) y++;
    }

    public void moveLeft() {
        if (x > 0) x--;
    }

    public void moveRight(int cols) {
        if (x < cols - 1) x++;
    }
}
