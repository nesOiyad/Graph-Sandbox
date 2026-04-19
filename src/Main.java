import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) throws Exception{
        JFrame jframe = new JFrame();

        jframe.setTitle("CSA Project");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new BorderLayout(10,5));
        jframe.setResizable(false);
        jframe.setSize(600,650);

        jframe.getContentPane().setBackground(Color.gray);

        GraphPanel panel = new GraphPanel();

        JPanel buttonPanel = new JPanel();

        JButton bfsButton = new JButton("BFS");
        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.BFS();
            }
        });

        JButton dijkstraButton = new JButton("Dijkstra");
        dijkstraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.Dijikstra();
            }
        });

        JButton aStarButton = new JButton("A*");
        aStarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.AStar();
            }
        });

        //adding reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setForeground(Color.RED); // Make the text red so it stands out
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.resetGraph();
            }
        });

        // Adding all the buttons to the panel
        buttonPanel.add(bfsButton);
        buttonPanel.add(dijkstraButton);
        buttonPanel.add(aStarButton);
        buttonPanel.add(resetButton); // Added Reset to the end

        jframe.add(panel, BorderLayout.CENTER);
        jframe.add(buttonPanel, BorderLayout.SOUTH);

        jframe.setVisible(true);
    }
}