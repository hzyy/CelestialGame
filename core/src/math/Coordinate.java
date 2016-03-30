package math;

public class Coordinate {
    private int x, y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }
}