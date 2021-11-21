package ResourceManagerPackage;

public class ResourceManagerDemo {
    public static void main(String[] args) throws Exception {
        ResourceManger model;
        if(args.length == 1){
            model = new ResourceManger(args[0]);
        }
        else{
            throw new Exception("Improper file input");
        }

        ResourceManagerGUI view = new ResourceManagerGUI();

        ResourceManagerController controller = new ResourceManagerController(model, view);

        controller.initController();
        
    }
}
