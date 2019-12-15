package Components.InterviewTree.Controller;


import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.InterviewTree.InterviewTreeCell;
import Components.InterviewTree.InterviewTreePluggable;
import Components.InterviewTree.visiter.CreateControllerVisitor;
import Components.InterviewTree.visiter.CreateInterviewTreeItemVisitor;
import application.Configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewTreeController implements Initializable {

    @FXML
    private
    TreeView<InterviewTreePluggable> interviewTree;

    private InterviewTreeRoot root;

    public InterviewTreeController(InterviewTreeRoot root) { this.root = root; }

    public static Node createInterviewTree(InterviewTreeController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewTree/InterviewTree.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        interviewTree.setEditable(true);
        interviewTree.setCellFactory(modelTreeElementTreeView -> {
            return new InterviewTreeCell();
        });
        setTreeRoot(root);
    }

    private void setTreeRoot(InterviewTreeRoot root) {
        CreateInterviewTreeItemVisitor visitor = new CreateInterviewTreeItemVisitor();
        root.accept(visitor);
        interviewTree.setRoot(visitor.getInterviewTreeElement());
    }
}