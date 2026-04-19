import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//importing things needed
public class GraphPanel extends JPanel{
    private ArrayList<node> nodes = new ArrayList<>();//creating array list for nodes
    private node selectedNode = null;
    private BufferedImage image;
    private ArrayList<edge> edges = new ArrayList<>();//creating array list for edges
    private node firstSelected = null;
    private char nextName = 'A';
    private ImageIcon customIcon;

    public GraphPanel() throws IOException
    {
        this.setBackground(Color.GRAY);
        image = ImageIO.read(new File("/node.png"));
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

                            // new window for the inputs
                            String weightStr = JOptionPane.showInputDialog(GraphPanel.this, "Please enter the weight of the edge:");
                            int weight = 0;

                            if (weightStr != null && !weightStr.trim().isEmpty()) {
                                try {
                                    weight = Integer.parseInt(weightStr);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(GraphPanel.this, "Invalid number! Defaulting weight to 0.");
                                }
                            }

                            edge newEdge = new edge(firstSelected,clickedNode, weight);

                            edges.add(newEdge);//a method that asks user to enter a weight from terminal (now a pop-up)
                            playEdgeSound();
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
                    playPopSound(); //plays the sound
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

    public int getEdgeWeight(node a, node b)// A method to find
    {
        for(edge e : edges)
        {
            if ((e.getStart() == a && e.getEnd() == b) || (e.getStart() == b && e.getEnd() == a))
            {
                return e.getWeight();
            }
        }
        return 0;
    }


    // Uses the math distance formula to find the physical pixel distance between nodes
    public double getHeuristic(node a, node b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy) / 100.0;
    }
    public void playEdgeSound() {
        try {
            File soundPath = new File("/edge.wav");

            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Warning: Could not find edge.wav");
            }
        } catch (Exception ex) {
            System.out.println("Error playing edge sound.");
        }
    }
    public void playPopSound() {
        try {
            // Look for the audio file right next to your images
            File soundPath = new File("/pop.wav");

            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start(); // Plays the sound!
            } else {
                System.out.println("Warning: Could not find pop.wav");
            }
        } catch (Exception ex) {
            System.out.println("Error playing sound.");
        }
    }
    public void playFindSound() {
        try {
            File soundPath = new File("/find.wav");

            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Warning: Could not find find.wav");
            }
        } catch (Exception ex) {
            System.out.println("Error playing find sound.");
        }
    }
    // Helper Method to get Start/End nodes from User UI
    private String[] getAlgorithmInputs() {
        String startName = JOptionPane.showInputDialog(this, "Please enter the starting vertex:");
        if (startName == null || startName.trim().isEmpty()) return null;

        String endName = JOptionPane.showInputDialog(this, "Please enter the ending vertex:");
        if (endName == null || endName.trim().isEmpty()) return null;

        return new String[]{startName.toUpperCase(), endName.toUpperCase()};
    }

    public void AStar() {
        String[] inputs = getAlgorithmInputs();
        if (inputs == null) return;
        String startName = inputs[0];
        String endName = inputs[1];

        node startNode = null;
        node endNode = null;

        for (node n : nodes) {//from the list we make first and second clicked nodes marks as startNode and endNode
            if(n.getName().equals(startName)) startNode = n;
            if(n.getName().equals(endName)) endNode = n;
        }

        if(startNode == null || endNode == null) {//if no value
            JOptionPane.showMessageDialog(this, "You didn't select valid starting or ending nodes.");
            return;
        }

        // Setup lists and maps
        ArrayList<node> openSet = new ArrayList<>();
        ArrayList<node> closedSet = new ArrayList<>();
        HashMap<node, Integer> gScore = new HashMap<>(); // Cost from start
        HashMap<node, Double> fScore = new HashMap<>();  // Cost from start + heuristic guess
        HashMap<node, node> previous = new HashMap<>();

        for (node n : nodes) {
            gScore.put(n, Integer.MAX_VALUE);
            fScore.put(n, Double.MAX_VALUE);
            previous.put(n, null);
        }

        gScore.put(startNode, 0);
        fScore.put(startNode, getHeuristic(startNode, endNode));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            // Find the node in the openSet with the lowest fScore
            node current = openSet.get(0);
            for (node n : openSet) {
                if (fScore.get(n) < fScore.get(current)) {
                    current = n;
                }
            }

            // If we reached the end, backtrack to get the path
            if (current == endNode) {
                String path = "";
                node back = endNode;
                while (back != null) {//backtracks the path we created
                    if (path.equals("")) {
                        path = back.getName();
                    } else {
                        path = back.getName() + "-->" + path;
                    }
                    back = previous.get(back);
                }
                playFindSound();

                JOptionPane.showMessageDialog(this, "A* shortest path is: " + path + "\nTotal distance cost: " + gScore.get(endNode), "A* Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            openSet.remove(current);
            closedSet.add(current);

            // Check neighbors
            for (node neighbor : getNeighbors(current)) {//checks evey neighbor and makes them add to hash map and make them pair
                if (closedSet.contains(neighbor)) {
                    continue; // Already evaluated this neighbor
                }

                // calculate tentative gScore
                int tentative_gScore = gScore.get(current) + getEdgeWeight(current, neighbor);

                // If we found a better path to this neighbor
                if (tentative_gScore < gScore.get(neighbor)) {
                    previous.put(neighbor, current);
                    gScore.put(neighbor, tentative_gScore);

                    // fScore is the real cost + the guessed remaining distance
                    fScore.put(neighbor, tentative_gScore + getHeuristic(neighbor, endNode));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "No path exists between " + startName + " and " + endName, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void Dijikstra()
    {
        String[] inputs = getAlgorithmInputs();
        if (inputs == null) return;
        String startName = inputs[0];
        String endName = inputs[1];

        node startNode = null;
        node endNode = null;

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

        if(startNode == null || endNode == null) //if no value
        {
            JOptionPane.showMessageDialog(this, "You didn't select valid starting or ending nodes.");
            return;
        }

        //neseccery things for dijisktra
        HashMap<node, Integer> distances = new HashMap<>();
        HashMap<node, node> previous = new HashMap<>();
        ArrayList<node> unvisited = new ArrayList<>();

        for (node n : nodes) {
            distances.put(n, Integer.MAX_VALUE);
            previous.put(n, null);
            unvisited.add(n);
        }
        distances.put(startNode, 0);

        while(!unvisited.isEmpty())
        {
            node current = null;
            int minDistance = Integer.MAX_VALUE;

            for (node n : unvisited) {
                if (distances.get(n) < minDistance) {
                    minDistance = distances.get(n);
                    current = n;
                }
            }
            if (current == null || current == endNode)
            {
                break;
            }

            unvisited.remove(current);

            for(node neighbor : getNeighbors(current))
            {
                if(unvisited.contains(neighbor))
                {
                    int weight = getEdgeWeight(current, neighbor);
                    int newDistance = distances.get(current) + weight;

                    if(newDistance < distances.get(neighbor))
                    {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                    }
                }
            }

        }

        String path = "";
        node back = endNode;

        if(previous.get(endNode) != null || endNode == startNode)
        {
            while (back != null)//backtracks the path we created
            {
                if(path.equals(""))
                {
                    path = back.getName();
                }
                else {
                    path = back.getName() + "-->" + path;
                }
                back = previous.get(back);
            }
            playFindSound();
            JOptionPane.showMessageDialog(this, "Dijkstra shortest path is: " + path + "\nTotal distance cost: " + distances.get(endNode), "Dijkstra Result", JOptionPane.INFORMATION_MESSAGE);

        }
        else
        {
            JOptionPane.showMessageDialog(this, "No path exists between " + startName + " and " + endName, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resetGraph() {//to add a button that resets the graph
        nodes.clear();
        edges.clear();
        nextName = 'A';
        selectedNode = null;
        firstSelected = null;
        repaint(); // Instantly clears the screen
    }

    public void BFS()
    {
        if(edges.isEmpty()){
            JOptionPane.showMessageDialog(this, "You need to draw edges first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int firstWeight = edges.get(0).getWeight();
        for(edge e : edges)//for BFS ever edge needs to have same weight, this loop checks that
        {
            int weight2 = e.getWeight();
            if(firstWeight != weight2)
            {
                JOptionPane.showMessageDialog(this, "For BFS, every edge needs to have the same weight!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String[] inputs = getAlgorithmInputs();
        if (inputs == null) return;
        String startName = inputs[0];
        String endName = inputs[1];

        node startNode = null;
        node endNode = null;

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
            JOptionPane.showMessageDialog(this, "You didn't selected the vertex you gonna use...");
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
            playFindSound();
            JOptionPane.showMessageDialog(this, "BFS path is: " + path, "BFS Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No path exists between " + startName + " and " + endName, "Error", JOptionPane.ERROR_MESSAGE);
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