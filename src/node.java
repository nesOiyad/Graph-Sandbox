import java.awt.image.BufferedImage;

public class node {

    private int x;
    private int y;
    private String name;

    private BufferedImage image;

    public node(int Nx, int Ny, BufferedImage Nimage, String Nname)// saving x and y coordinates of nodes
    {
        x = Nx;
        y = Ny;
        image = Nimage;
        name = Nname;
    }
    // Checks if the mouse is on the node (or not), techniaclly creates a hitbox
    public boolean hitBox(int mouseX, int mouseY)
    {
        return (mouseX >= x && mouseX <= x + 60 && mouseY >= y && mouseY <= y + 60);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getName()
    {
        return name;
    }

    public void setX(int newX) {
        x = newX;
    }
    public void setY(int newY) {
        y = newY;
    }



}
