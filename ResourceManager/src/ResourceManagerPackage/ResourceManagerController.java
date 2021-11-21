package ResourceManagerPackage;

public class ResourceManagerController {
    private final ResourceManger model;
    private final ResourceManagerGUI view;

    //controller constructor with model and view used as parameters
    public ResourceManagerController(ResourceManger model, ResourceManagerGUI view){
        this.model = model;
        this.view = view;
    }

    //adds functionality to gui
    public void initController(){
        view.getStartBtn().addActionListener(e -> startManager(model.getNumProcesses(), model.getNumResources(), model.getAvailable()));
        view.getNextBtn().addActionListener(e -> nextStep(model.getNumProcesses(), model.getNumResources(),
                model.getAvailable(), model.getWaitMatrix(), model.getHoldMatrix()));
    }

    //sets up panel with graphics for simulation
    private void startManager(int numProcesses, int numResources, int[] available){
        view.getStartBtn().setVisible(false);
        view.getNextBtn().setVisible(true);
        view.setUpPanel(numProcesses, numResources, available);
    }

    //updates panel with graphics for simulation
    private void nextStep(int numProcesses, int numResources, int[] available, int[][] wait, int[][] hold){
        model.executeNextLine();
        view.redrawPanel(numProcesses, numResources, available, wait, hold);
    }
}
