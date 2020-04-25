package components.interviewSelector.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.interviewSelector.controllers.ModifyInterviewController;
import components.interviewSelector.modelCommands.EditInterviewCommand;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Interview;
import models.Project;
import utils.DialogState;

import java.io.IOException;

public class ModifyInterviewCommand extends InterviewSelectorCommand<Void>  {
    Interview interview;
    public ModifyInterviewCommand(Project project, Interview interview) {
        super(project);
        this.interview = interview;
    }

    @Override
    public Void execute() {
        ModifyInterviewController controller = ModifyInterviewController.createController();
        controller.setInterview(interview);
        controller.initData();
        controller.show();
        if (controller.getState()== DialogState.SUCCESS){
            HistoryManager.addCommand(new EditInterviewCommand(interview, controller.getDate(), controller.getParticipantName(), controller.getComment() ),true);
        }
        return null;
    }
}
