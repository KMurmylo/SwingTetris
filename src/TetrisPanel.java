import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TetrisPanel extends JPanel implements ActionListener {
    Tetris game;
    Timer timer;
    public TetrisPanel(Tetris game){
        this.game=game;
        game.panel=this;
        timer=new Timer(40,this);
        timer.start();

    }
    public void actionPerformed(ActionEvent a){
        repaint();

    }
    @Override public void paintComponent(Graphics g){

        for(int i=0;i<game.xSize;i++){
            for(int j=0;j<game.ySize;j++){
                g.setColor( game.table[i][j] );
                g.fillRect(5+i*40,j*40,37,37);
            }
        }
        g.setColor(game.activePiece.color);
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++)
            {if(game.activePiece.shape[i][j])
                g.fillRect(5+((game.activePiecex+i)*40)
                        ,(game.activePiecey+j)*40
                        ,37,37);
            }}


    }
}
