package ResourceManagerPackage;

import javax.swing.*;
import java.awt.*;

public class ResourceManagerPanel extends JPanel{
    public int numCircles;
    public int numRectangles;
    public int[] numEach;
    public int[][] circleToRectangle;
    public int[][] rectangleToCircle;

    public void paintComponent(Graphics g){
        int circleX = 0;
        int circleY = 50;
        int rectX = 0;
        int rectY = 200;


        super.paintComponent(g);
        setBackground(Color.WHITE);

        //paints the process on the panel
        g.setColor(Color.ORANGE);
        for(int i = 0; i < numCircles; i++){
            g.fillOval(circleX, circleY, 50, 50);
            g.setColor(Color.BLACK);
            g.drawString("P" + i, circleX + 20, circleY + 25);
            circleX += 100;
            g.setColor(Color.ORANGE);
        }

        //paints the resources on the panel
        g.setColor(Color.cyan);
        for(int i = 0; i < numRectangles; i++){
            g.fillRect(rectX, rectY, 50, 50);
            g.setColor(Color.BLACK);
            g.drawString("R" + i + ": " + String.valueOf(numEach[i]), rectX + 18, rectY + 25);
            rectX += 100;
            g.setColor(Color.cyan);
        }

        //draws arrow from resource to process when process is holding an instance of the resource
        if(rectangleToCircle != null){
            g.setColor(Color.BLACK);
            rectX = 0;
            circleX = 15;
            double phi = Math.toRadians(40);
            double barb = 20;
            for(int i = 0; i < rectangleToCircle.length; i++){
                for(int j = 0; j < rectangleToCircle[i].length; j++){
                    if(rectangleToCircle[i][j] > 0){
                        g.drawLine(rectX, rectY, circleX, circleY + 50);
                        double dy = (circleY + 50) - rectY;
                        double dx = circleX - rectX;
                        double theta = Math.atan2(dy, dx);
                        double x, y, rho = theta + phi;
                        for(int k = 0; k < 2; k++) {
                            x = circleX - barb * Math.cos(rho);
                            y = (circleY + 50) - barb * Math.sin(rho);
                            g.drawLine(circleX, circleY + 50, (int) x, (int) y);
                            rho = theta - phi;
                        }
                    }
                    rectX += 100;
                }
                rectX = 0;
                circleX += 100;
            }
        }

        //draws and arrow from the process to the resource when the process is waiting on an instance of the resource
        if(circleToRectangle != null){
            g.setColor(Color.red);
            rectX = 50;
            circleX = 35;
            double phi = Math.toRadians(40);
            double barb = 20;
            for(int i = 0; i < circleToRectangle.length; i++){
                for(int j = 0; j < circleToRectangle[i].length; j++){
                    if(circleToRectangle[i][j] > 0){
                        g.drawLine(circleX, circleY + 50, rectX, rectY);
                        double dy = rectY - (circleY + 50);
                        double dx = rectX - circleX;
                        double theta = Math.atan2(dy, dx);
                        double x, y, rho = theta + phi;
                        for(int k = 0; k < 2; k++) {
                            x = rectX - barb * Math.cos(rho);
                            y = rectY - barb * Math.sin(rho);
                            g.drawLine(rectX, rectY, (int) x, (int) y);
                            rho = theta - phi;
                        }
                    }
                    rectX += 100;
                }
                rectX = 50;
                circleX += 100;
            }
        }


    }

    //tells panel how many processes to draw
    public void setNumCircles(int numCircles) {
        this.numCircles = numCircles;
    }

    //tells panel how many resources to draw
    public void setNumRectangles(int numRectangles) {
        this.numRectangles = numRectangles;
    }

    //tells panel how many instances of each resource
    public void setNumEach(int[] numEach) {
        this.numEach = numEach;
    }

    //tells panel which arrows to draw from process to resource
    public void setCircleToRectangle(int[][] circleToRectangle) {
        this.circleToRectangle = circleToRectangle;
    }

    //tells panel which arrows to draw from resource to process
    public void setRectangleToCircle(int[][] rectangleToCircle){
        this.rectangleToCircle = rectangleToCircle;
    }
}
