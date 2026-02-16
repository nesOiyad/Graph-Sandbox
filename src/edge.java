import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class edge {

    private node start;
    private node end;
    private int weight;
    private Scanner input = new Scanner(System.in);

    public edge(node eStart, node eEnd, int eWeight )//saving end and start side of edges created
    {
        start = eStart;
        end = eEnd;
        weight = eWeight;


    }
    public node getStart()
    {
        return start;
    }
    public node getEnd()
    {
        return end;
    }

    public int getWeight()
    {
        return weight;
    }
    public void setWeight(int num)//asks user to determine wieght of the edge
    {
        System.out.println("Please enter the weight of the edge: ");
        num = input.nextInt();
        weight = num;

    }







}
