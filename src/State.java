public class State {

    private ColorValue c;
    private Point p;
    private boolean reached;

    public State (ColorValue c, Point p){
        this.c = c;
        this.p = p;
    }

    public void setColorValue(ColorValue c) {
        this.c = c;
    }

    public ColorValue getColorValue(){
        return c;
    }
    public Point getPoint(){
        return p;
    }

    public boolean isReached(){
        return reached;
    }

    @Override
    public String toString() {
        return "(" + c +
                 p + ")"
                ;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
