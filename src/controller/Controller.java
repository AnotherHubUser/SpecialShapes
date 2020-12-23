package controller;
import model.shapes.Shape;
import model.backpack.Backpack;
import model.backpack.BackpackManager;
import view.Dialoge.AddBackpackDialoge;
import view.Dialoge.DeleteDialoge;
import view.View;
import view.Dialoge.AddDialoge;

public class Controller {
    private View view_;
//    private Backpack<Shape> shapes_;
    private BackpackManager<Shape> manager_;
    private XMLCommunicator communicator_;

    public Controller() {
        this.view_ = new View(1500, 750, "Welcome to the Shapes party!", this);
        this.manager_ = new BackpackManager<>();
//        shapes_ = new Backpack<>();
        manager_.addBackpack(new Backpack<>());
        communicator_ = new XMLCommunicator();
    }

    public void Go() {
        view_.Show();
    }

    public void update(){
        view_.updateShapes(manager_.getCurrentBackpack().getItems(),
                manager_.getCurrentBackpack().size(),
                manager_.getCurrentBackpack().capacity());
    }

    public void addShape(Shape shape) {
        try {
            manager_.getCurrentBackpack().put(shape);
        } catch (Exception exception) {
            view_.showException(exception);
        }
        update();
    }


    public void openAddDialoge(Controller controller) {
        AddDialoge dialog = new AddDialoge(view_, 400, 740, true, this);
        dialog.setTitle("Wanna Shape?");
        dialog.setVisible(true);
    }

    public void addBackpack() {
        AddBackpackDialoge dialog = new AddBackpackDialoge(view_, 400, 600, true, this);
        dialog.setTitle("Wanna Backpack?");
        dialog.setVisible(true);
    }

    public void addBackpack(Double size, String name) {
        Backpack<Shape> backpack = new Backpack<Shape>(size);
        manager_.addBackpack(backpack);
        view_.addBackpack(name);
        update();
    }

    public void deleteShapes(Controller controller) {
        DeleteDialoge dialog = new DeleteDialoge(view_, 400, 600, true, this);
        dialog.setTitle("Wanna delete shapes?");
        dialog.setVisible(true);
        update();
    }

    public Integer getNumberOfBackpacks(){
        return manager_.getNumberOfBackpacks();
    }

    public void setCurrentBackpack(Integer index){
        manager_.setCurrentBackpack(index);
        update();
    }

    public void removeShapes() {
        deleteShapes(this);
//        for(int i = manager_.getCurrentBackpack().getItems().size(); i > 0; i--){
//            manager_.getCurrentBackpack().erase(manager_.getCurrentBackpack().getItems().get(i-1));
//        }
//        view_.updateShapes(manager_.getCurrentBackpack().getItems(),
//                manager_.getCurrentBackpack().size(),
//                manager_.getCurrentBackpack().capacity());
    }

    public void removeShape(String name){
        manager_.getCurrentBackpack().erase(
                manager_.getCurrentBackpack().getItems().get(Integer.parseInt(name)));
    }

    public void save() {
        try {
            String path = view_.getSavePath();
            if (path == null) {
                return;
            }
            communicator_.saveShapesToXML(path, manager_.getCurrentBackpack());
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void load() {
        try {
            String path = view_.getLoadPath();
            if (path == null) {
                return;
            }
            Backpack<Shape> LoadedBackpack = communicator_.loadShapesFromXML(path);
            if (LoadedBackpack != null) {
//                removeShapes();
                manager_.addBackpack(LoadedBackpack);

//                // its better to upload new backpack to avoid problems with size of backpack
//                shapes_ = LoadedBackpack;
                view_.updateShapes(manager_.getCurrentBackpack().getItems(),
                        manager_.getCurrentBackpack().size(),
                        manager_.getCurrentBackpack().capacity());
            }
        } catch (Exception e) {
            handleException(e);
        }
        update();
    }

    public void handleException(Exception e) {
        view_.showException(e);
    }
}
