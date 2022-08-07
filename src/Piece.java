import java.awt.*;

class Piece{
    Color color=Color.BLUE;
    public Boolean[][] shape;
    public Piece copy(){
        Piece temp =new Piece();
        temp.shape=this.shape.clone();
        temp.color=this.color;
        return temp;
    }
    Piece(){}

    Piece(Boolean[][] b){
        shape=b;
    }
}