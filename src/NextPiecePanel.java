import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPiecePanel extends JPanel implements ActionListener {
    Tetris game;
    public NextPiecePanel(Tetris game){
        this.game=game;

    }
    public void actionPerformed(ActionEvent a){
        repaint();

    }
    @Override public void paintComponent(Graphics g){

    g.setColor(game.nextPiece.color);
        for(int i=0;i<4;i++){
        for(int j=0;j<4;j++)
        {if(game.nextPiece.shape[i][j])
            g.fillRect(i*40
                    ,j*40
                    ,37,37);
        }}
    }
}
