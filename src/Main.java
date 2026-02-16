import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Main {
    public static void main(String[] args) throws Exception{
        //this is the place where it runs the methods and creates a panel for us.
        JFrame jframe = new JFrame();

        jframe.setTitle("CSA Project");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new BorderLayout(10,5));
        jframe.setResizable(false);
        jframe.setSize(600,650);

        jframe.getContentPane().setBackground(Color.gray);

        JButton bfsButton = new JButton("BFS");


        GraphPanel panel = new GraphPanel();
        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.BFS();
            }
        });


        jframe.add(panel, BorderLayout.CENTER);
        jframe.add(bfsButton, BorderLayout.SOUTH);

        jframe.setVisible(true);

    }
}