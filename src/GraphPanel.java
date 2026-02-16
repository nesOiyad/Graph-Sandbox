import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
//importing things needed
public class GraphPanel extends JPanel{
    private ArrayList<node> nodes = new ArrayList<>();//creating array list for nodes
    private node selectedNode = null;
    private BufferedImage image;
    private ArrayList<edge> edges = new ArrayList<>();//creating array list for edges
    private node firstSelected = null;
    private Scanner input = new Scanner(System.in);
    private char nextName = 'A';

    public  GraphPanel() throws IOException
    {
        image = ImageIO.read(new File("node.png"));
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {//thanks to swing library I can track mouse's input, using MouseEvent object
                node clickedNode = null;
                if(SwingUtilities.isLeftMouseButton(e))//checking if it left clicks
                {
                    for(node n : nodes)//it looks to every node in nodes array
                    {
                        if(n.hitBox(e.getX(), e.getY()))//hitBox checks if mouse is toueching to not and if yes marks it as clickedNode and breaks the loop
                        {
                            clickedNode = n;
                            break;
                        }
                    }
                    if (e.isShiftDown() && clickedNode != null)//if it is marked as clicked and pressing shift
                    {
                        if(firstSelected == null)
                        {
                            firstSelected = clickedNode;
                            //System.out.println("First has been selected");
                        }
                        else if (firstSelected != clickedNode)//if selected node is not clicked(mouse clicking on it)
                        {

                            edge newEdge = new edge(firstSelected,clickedNode, 0);
                            newEdge.setWeight(0);

                            edges.add(newEdge);//a method that asks user to enter a weight from terminal
                            firstSelected = null;
                            repaint();
                        }
                    }
                    else if (!e.isShiftDown()) {
                        selectedNode = clickedNode;
                    }

                }

                else if (SwingUtilities.isRightMouseButton(e))//if right clicked
                {
                    nodes.add(new node(e.getX() - 25,e.getY() - 25, image , String.valueOf(nextName)));//adding new node to array with these x and y corrdinates
                    nextName++;//this is a char when adding
                    repaint();//this method paints to the panel

                }

                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {//when mouse released

                selectedNode = null;
            }
        });

        addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e) {//if it is selected every time we drag it changes its coordinates
                if(selectedNode != null)
                {
                    selectedNode.setX(e.getX() - 25);
                    selectedNode.setY(e.getY() - 25);
                    repaint();
                }
            }
        });

    }
    public ArrayList<node> getNeighbors(node n)//a getNeighbor method that from node array
    {
        ArrayList<node> neighbors = new ArrayList<>();//creating a new array list
        for(edge e : edges)
        {
            if(e.getStart() == n)
            {
                neighbors.add(e.getEnd());
            }
            else if(e.getEnd() == n)
            {
                neighbors.add(e.getStart());


            }
        }
        return neighbors;
    }
    public void BFS()
    {
        System.out.println("Please enter the starting vertex: ");
        String startName = input.nextLine();
        System.out.println("Please enter the ending vertex");
        String endName = input.nextLine();

        node startNode = null;
        node endNode = null;
        int firstWeight = edges.get(0).getWeight();
        for(edge e : edges)//for BFS ever edge needs to have same weight, this loop checks that
        {
            int weight2 = e.getWeight();
            if(firstWeight != weight2)
            {
                System.out.println("Every edge needs to have same weight!");
                break;
            }


        }
        for (node n : nodes)//from the list we make first and second clicked nodes marks as startNode and endNode
        {
            if(n.getName().equals(startName))
            {
                startNode = n;
            }
            if(n.getName().equals(endName))
            {
                endNode = n;
            }
        }
        if(startName == null || endName == null)//if both are didn't selected
        {
            System.out.println("You didn't selected the vertex you gonna use...");
            return;

        }
        ArrayList<node> queue = new ArrayList<>();//for bfs when we check
        // every node, first we start with 1 edge away nodes and add them to queue, then we mark these nodes
        //we make every node in queue visited until we make aimed node visited
        ArrayList<node> visited = new ArrayList<>();
        HashMap<node, node> pairs = new HashMap<>();//we need hash map for identfying connected nodes
        queue.add(startNode);//automatacly makes starting node visited
        visited.add(startNode);

        boolean found = false;

        while(!(queue.isEmpty()))//this loop continues until clears queue
        {
            node current = queue.remove(0);

            if(current == endNode)
            {
                found = true;
                break;

            }

            for (node neighbor : getNeighbors(current)) {//checks evey neighbor and makes them add to hash map and make them pair
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    pairs.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        String path = "";
        node back = endNode;
        if(found)
        {
            while (back != null)//backtracks the path we created
            {
                if (path.equals(""))
                {
                    path = back.getName();
                }
                else
                {
                    path = back.getName() + "-->" + path;
                }
                back = pairs.get(back);
            }

            System.out.println("BFS path is: " + path);
        }



        }

    protected void paintComponent(Graphics g) {//this is the method we use to paint things on panel
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.CYAN);
        for(edge e : edges)
        {

            int x1 = e.getStart().getX() + 25;
            int x2 = e.getEnd().getX() + 25;
            int y1 = e.getStart().getY() + 25;
            int y2 = e.getEnd().getY() + 25;

            g.drawLine(x1,y1,x2,y2);

            g.setColor(Color.RED);

            g.drawString(Integer.toString(e.getWeight()), (x1 + x2) / 2, (y1 + y2) / 2);
            g.setColor(Color.CYAN);

        }
        g2.setStroke(new BasicStroke(1));

        for (node n : nodes) {//painting every node with specific font etc.

            g.drawImage(n.getImage(), n.getX(), n.getY(), 50, 50, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

            g.drawString(n.getName(), n.getX() + 18, n.getY() + 32);
        }
    }










}
