import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

class Tetris  {
    Action goLeft;
    Action goRight;
    Action rotate;
    Action goDown;
    JPanel panel;
    JFrame frame;
    int xSize=8;
    int ySize=14;
    int tickrate=500;
    Piece activePiece;
    Piece nextPiece;
    int activePiecex;
    int activePiecey;
    Random rng;
    public Color[][] table;
    private Piece[] pieces;
    Timer tickTimer;
    int score;
    JLabel lScore;
    Color[] colors;
    JButton pauseButton;

    NextPiecePanel nextPiecePanel;
    enum gameStates{
        Running,
        Paused,
        Over
    }
    gameStates currentState;



    Tetris(){
        table=new Color[xSize][ySize];
        for(int i=0;i<xSize;i++)
            for(int j=0;j<ySize;j++){
                table[i][j]=Color.black;
            }
        rng=new Random();
        setupPieces();
        setupColors();
        preparePiece();
        spawnPiece();
        frame=new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500,600);

        TetrisPanel panel=new TetrisPanel(this);
        panel.setSize(40*xSize,40*ySize);
        panel.setBackground(Color.lightGray);

        nextPiecePanel=new NextPiecePanel(this);
        nextPiecePanel.setBounds(330,200,160,160);
        nextPiecePanel.setBackground(Color.GREEN);
        frame.add(nextPiecePanel);



        rotate=new Rotate();
        goLeft=new GoLeft();
        goRight=new GoRight();
        goDown=new Tick();

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"),"Rotate");
        panel.getActionMap().put("Rotate",rotate);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"),"GoLeft");
        panel.getActionMap().put("GoLeft",goLeft);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"),"GoRight");
        panel.getActionMap().put("GoRight",goRight);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),"GoDown");
        panel.getActionMap().put("GoDown",goDown);
        tickTimer=new Timer(tickrate,e -> tick());


        lScore=new JLabel();
        lScore.setBounds(330,10,160,20);
        lScore.setFont(new Font("Arial", Font.PLAIN,20));
        lScore.setText("Score: "+score);
        frame.add(lScore);
        frame.add(panel);
        pauseButton=new JButton("Pause");

        pauseButton.setBounds(330,50,160,20);
        pauseButton.addActionListener(new Pause());
        frame.add(pauseButton);
        frame.setVisible(true);
        frame.repaint();

        score=0;
        tickTimer.start();
        currentState=gameStates.Running;

    }
     private void setupPieces(){
        pieces=new Piece[5];
        pieces[0]=new Piece();
        pieces[0].shape= new Boolean[][]{
                {true, true, false, false},
                {true, true, false, false},
                {false, false, false, false},
                {false, false, false, false}};
        pieces[1]=new Piece();
        pieces[1].shape= new Boolean[][]{
                {true, false, false, false},
                {true, false, false, false},
                {true, false, false, false},
                {true, false, false, false}};
        pieces[2]=new Piece();
        pieces[2].shape= new Boolean[][]{
                {true, true, false, false},
                {true, false, false, false},
                {true, false, false, false},
                {false, false, false, false}};
        pieces[3]=new Piece();
        pieces[3].shape= new Boolean[][]{
                {true, true, false, false},
                {false, true, true, false},
                {false, false, false, false},
                {false, false, false, false}};
        pieces[4]=new Piece();
        pieces[4].shape= new Boolean[][]{
                {false, true, false, false},
                {true, true, true, false},
                {false, false, false, false},
                {false, false, false, false}};


    }
    private void setupColors(){
        colors=new Color[]{Color.BLUE,Color.red,Color.green,Color.yellow,Color.orange,Color.gray,Color.pink,Color.cyan};
    }
    public void preparePiece(){
        nextPiece=pieces[rng.nextInt(pieces.length)].copy();
        nextPiece.color=colors[rng.nextInt(colors.length)];
    }
    public void spawnPiece(){
//        activePiece=pieces[rng.nextInt(pieces.length)].copy();
//        activePiece.color=colors[rng.nextInt(colors.length)];

        activePiece=nextPiece;


        activePiecex=2;
        activePiecey=0;
        if(checkCollision(activePiece.shape))
            {tickTimer.stop();
            gameOver();}
    }
    public void tick(){
        if(checkCollisionDown())
        {   pastePiece();
            checkLanes();

            spawnPiece();
            preparePiece();
            frame.repaint();
        }else
            activePiecey++;
//        try {
//            Thread.sleep(tickrate);
//        }catch (InterruptedException e)
//        {
//            Thread.currentThread().interrupt();
//        }



    }

    private boolean checkCollision(Boolean[][] temp){
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    if(temp[i][j])
                    {
                        if(!(activePiecey+j<ySize)||!(activePiecex+i<xSize)||table[activePiecex+i][activePiecey+j+1]!=Color.black)
                        {return true;}
                    }
            return false;
        }

    private boolean checkCollisionDown(){
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                if(activePiece.shape[i][j])
                {
                    if(!(activePiecey+j+1<ySize)||table[activePiecex+i][activePiecey+j+1]!=Color.black)
                    {return true;}
                }
        return false;
    }
    private boolean checkCollisionRight(){
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                if(activePiece.shape[i][j])
                {
                    if((activePiecex+i+1>=xSize)||table[activePiecex+i+1][activePiecey+j]!=Color.black)
                    {return true;}
                }
        return false;
    }
    private boolean checkCollisionLeft(){
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                if(activePiece.shape[i][j])
                {
                    if(!(activePiecex+i>0)||table[activePiecex+i-1][activePiecey+j]!=Color.black)
                    {return true;}
                }
        return false;
    }
    private void pastePiece(){
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                if(activePiece.shape[i][j])
                    table[activePiecex+i][activePiecey+j]=activePiece.color;
    }
    private class GoLeft extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
        if(!checkCollisionLeft())
            activePiecex--;
        }
    }
    private class GoRight extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {

        if(!checkCollisionRight())
            activePiecex++;
        }
    }
    private class Rotate extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
        rotate();
        }
    }
    private class Tick extends AbstractAction{
        public void actionPerformed(ActionEvent e)
        {tick();}
    }
    private class Pause implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (currentState) {
                case Running -> {
                    pause();
                    pauseButton.setText("Unpause");
                    currentState = gameStates.Paused;
                }
                case Paused -> {
                    unpause();
                    pauseButton.setText("Pause");
                    currentState = gameStates.Running;
                }
                case Over -> {
                    restart();
                    tickTimer.start();
                    currentState = gameStates.Running;
                    pauseButton.setText("Pause");
                }
            }
            frame.repaint();
        }
    }
    public void rotate(){
        Boolean[][] temp =new Boolean[4][4];
        Boolean tempBool;
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                temp[i][j]=activePiece.shape[j][i];
        for(int i=0;i<2;i++)
            for(int j=0;j<4;j++)
            {   tempBool=temp[i][j];
                temp[i][j]=temp[3-i][j];
                temp[3-i][j]=tempBool;}
        pushPieceLeft(temp);
        pushPieceUp(temp);
        if(!checkCollision(temp))
        {activePiece.shape=temp;}
    }
    public Boolean checkUp(Boolean[][] temp){

        for(int i=0;i<4;i++)
            if(temp[i][0])
                return false;
        return true;
    }
    public Boolean checkLeft(Boolean[][] temp){
        for(int i=0;i<4;i++)
            if(temp[0][i])
                return false;
        return true;
    }
    public void pushPieceUp(Boolean[][] temp){
       while(checkUp(temp)){
           for(int i=0;i<4;i++)
               for(int j=0;j<3;j++)
                   temp[i][j]=temp[i][j+1];
        for(int i=0;i<4;i++)
            temp[i][3]=false;}
    }
    public void pushPieceLeft(Boolean[][] temp){
        while(checkLeft(temp)){

            for(int i=0;i<3;i++)
                for(int j=0;j<4;j++)
                    temp[i][j]=temp[i+1][j];
            for(int i=0;i<4;i++)
                temp[3][i]=false;}
    }
    public void pause(){
        tickTimer.stop();
    }
    public void unpause(){
        tickTimer.start();
    }
    private Boolean checkLane(int row){
        for(int i=0;i<xSize;i++)
            if(table[i][row]==Color.black) {return false;}
        return true;
    }
    private void checkLanes()
    {   int combo=0;
        for(int i=0;i<ySize;i++){
            if(checkLane(i)) {
                score+=100+(combo*40);
                combo++;
                pushDownLanes(i);
            }


        }
        if(combo>0){
            lScore.setText("Score: "+score);
            frame.repaint();}

    }
    private void pushDownLanes(int startingLane){
        for(int i=0;i<xSize;i++)
            for(int j=startingLane;j>0;j--)
                table[i][j]=table[i][j-1];
        for(int i=0;i<xSize;i++)
            table[i][0]=Color.black;
    }

    public void restart(){
        table=new Color[xSize][ySize];
        for(int i=0;i<xSize;i++)
            for(int j=0;j<ySize;j++){
                table[i][j]=Color.black;
                //System.out.print(i+" "+j+ "\n");
            }
        score=0;
        frame.repaint();
        spawnPiece();

    }
    private void gameOver(){
      Scoreboard scoreBoard=new Scoreboard();
      scoreBoard.sendScore(score);
      pauseButton.setText("Restart");
      currentState=gameStates.Over;

      frame.repaint();
    }

}