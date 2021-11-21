package ResourceManagerPackage;

import javax.swing.*;
import java.awt.*;

public class ResourceManagerGUI extends JFrame {
    //components of the gui
    private final JFrame resourceManagerFrame;
    private ResourceManagerPanel resourceManagerPanel;
    private JPanel contentPane;
    private final JButton nextBtn;
    private final JButton startBtn;

    public ResourceManagerGUI(){
        //sets up jframe that will hold all components
        resourceManagerFrame = new JFrame();
        resourceManagerFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        resourceManagerFrame.setTitle("Resource Manager Simulation");

        //creates content pane that will hold all panels
        contentPane = new JPanel(new BorderLayout());

        //creates panel that holds buttons and adds to content pane
        JPanel buttonPanel = new JPanel();
        startBtn = new JButton("Start");
        buttonPanel.add(startBtn);
        startBtn.setVisible(true);
        nextBtn = new JButton("Next Step");
        buttonPanel.add(nextBtn);
        nextBtn.setVisible(false);
        contentPane.add(buttonPanel, BorderLayout.PAGE_START);
        buttonPanel.setVisible(true);

        //creates panel that will hold the graphics of simulation and adds to content pane
        resourceManagerPanel = new ResourceManagerPanel();
        contentPane.add(resourceManagerPanel, BorderLayout.CENTER);
        resourceManagerPanel.setVisible(false);

        //adds content pane to jframe and sets visible to the user
        resourceManagerFrame.setContentPane(contentPane);
        resourceManagerFrame.pack();
        resourceManagerFrame.setVisible(true);
    }

    //sets up panel that holds graphics
    public void setUpPanel(int numProcesses, int numResources, int[] available){
        //adjusts size of jframe to fit graphics
        if(numProcesses > numResources){
            resourceManagerFrame.setSize((100 * numProcesses), 400);
        }else{
            resourceManagerFrame.setSize((100 * numResources), 400);
        }

        //indicates how many processes and resources to draw with how many instances and sets visible to user
        resourceManagerPanel.setNumCircles(numProcesses);
        resourceManagerPanel.setNumRectangles(numResources);
        resourceManagerPanel.setNumEach(available);
        resourceManagerPanel.setVisible(true);


    }

    public void redrawPanel(int numProcesses, int numResources, int[] available, int[][] wait, int[][] hold){
        //updates panel to show which processes are waiting and holding
        resourceManagerPanel.setNumCircles(numProcesses);
        resourceManagerPanel.setNumRectangles(numResources);
        resourceManagerPanel.setNumEach(available);
        resourceManagerPanel.setCircleToRectangle(wait);
        resourceManagerPanel.setRectangleToCircle(hold);
        resourceManagerPanel.revalidate();
        resourceManagerPanel.repaint();
    }

    //returns the button that starts the simulation
    public JButton getStartBtn(){
        return startBtn;
    }

    //returns the button that takes the simulation to the next step in the input file
    public JButton getNextBtn() { return nextBtn; }
}
