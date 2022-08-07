import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Scoreboard {
    String path;
    File file;
    List<JLabel> labels;
    JFrame frame;
        Scoreboard(){
            labels=new ArrayList<>();
            path=System.getProperty("user.dir")+"\\Score.txt";
            frame=new JFrame();
            frame.addWindowListener(
                    new java.awt.event.WindowAdapter(){
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent){

                        }
                    }
            );
            frame.setLayout(null);
            frame.setSize(400,700);
            frame.setLocation(300,300);
            file=new File(path);
            if(file.exists())
                showScore(readScore());


            frame.setVisible(true);
        }
        private void write(String[] input){
            try{
                BufferedWriter bw=new BufferedWriter(new FileWriter(path));
                for (String x:input) {
                    bw.write(x+"\n");
                }
                bw.close();
            }
            catch(Exception e){
                JOptionPane.showInputDialog("Error while writing to file");
            }

        }
        private void write(List<String> input){
//            System.out.println("trying to create at "+path);
            try{
                BufferedWriter bw=new BufferedWriter(new FileWriter(path));
                for (String x:input) {
                    bw.write(x+"\n");
                }
                bw.close();
            }
            catch(Exception e){
                JOptionPane.showInputDialog("Error while writing to file");
            }
        }

        private List<String> readScore(){
            List<String> list=new ArrayList<>();
            try{
            BufferedReader br=new BufferedReader(new FileReader(path));
            String s;

            while((s=br.readLine())!=null){
                    list.add(s);
            }
            br.close();
            }catch(Exception e){
            list.add("Error Reading File");

            }
            return list;
        }
        public void sendScore(int score){
            int i=0;
            List<String> list=readScore();
            int[] scores=new int[list.size()];
            for(String s:list){
                scores[i++]=Integer.parseInt(s.split(":")[1]);
            }

            try{
                int min=Arrays.stream(scores).min().getAsInt();

                if(score>min)
                {   i=0;
                    while(scores[i]>=score)
                        i++;
                    String name=JOptionPane.showInputDialog(frame,"Your score of "+score+" is on the leaderboard, please enter your name");
                    if(name!=null&&!name.equals("")){
                    list.add(i,name+":"+score);}
                if(list.size()>10)
                    list.remove(10);
                write(list);}}
            catch(NoSuchElementException e)
            {   String name=JOptionPane.showInputDialog(frame,"Your score of "+score+" is on the leaderboard, please enter your name");
                if(name!=null&&!name.equals("")){
                list.add(name+":"+score);
                write(list);}
            }

            showScore(readScore());

        }
        private void showScore(List<String> scores){
            JLabel label;
            int i=0;
            for(JLabel l : labels)
            {
                frame.remove(l);
            }
            labels.clear();
            for (String x:scores) {
                label=new JLabel(x);
                labels.add(label);
                label.setBounds(10,10+(40*i),200,35);
                i++;
                frame.add(label);
            }
            frame.repaint();
        }
}
