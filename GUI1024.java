package Project2;

import javax.swing.*;

public class GUI1024 {
    public static void main(String arg[]){
        JMenuBar menu= new JMenuBar();
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem setGoal = new JMenuItem("Set Goal");
        JMenuItem quitItem= new JMenuItem("Quit");
        JMenuItem resize = new JMenuItem("Resize");
       
        JFrame gui = new JFrame ("Welcome to 1024!");
        menu.add(reset);
        menu.add(setGoal);
        menu.add(resize);
        menu.add(quitItem);
        gui.setJMenuBar(menu);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boolean flag=true;
        int width=0;
        int height=0;
        int goal=0;
        String a;
        while(flag==true){
            a=JOptionPane.showInputDialog(null, "Width : ", "4");
            try{
                width = Integer.parseInt(a);
                if(width<0){
                    throw new IllegalArgumentException();
                }
                else{
                    flag=false;
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Invalid Input");
            }
        }

        flag=true;
        while(flag==true){
            a=JOptionPane.showInputDialog(null, "Height : ", "4");
            try{
                height = Integer.parseInt(a);
                if(height<0){
                    throw new IllegalArgumentException();
                }
                else{
                    flag=false;
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Invalid Input");
            }
        }
        flag=true;
        while(flag==true){
            a=JOptionPane.showInputDialog(null, "Goal : ", "2048");
            try{
                goal = Integer.parseInt(a);
                if(goal<0 || (goal&goal-1)!=0){
                    throw new IllegalArgumentException();
                }
                else{
                    flag=false;
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Invalid Input");
            }
        }
        GUI1024Panel panel = new GUI1024Panel(gui,width,height,goal,reset,setGoal,resize,quitItem);
        //panel.setFocusable(true);
        gui.getContentPane().add(panel);
        System.out.println();
        gui.setSize(panel.getSize());
        gui.setVisible(true);
    }
}