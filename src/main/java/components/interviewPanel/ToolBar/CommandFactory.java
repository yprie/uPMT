package components.interviewPanel.ToolBar;

import components.interviewPanel.Controllers.InterviewTextController;

public class CommandFactory {
    InterviewTextController interviewTextController;


    public CommandFactory(InterviewTextController interviewTextController){
        this.interviewTextController = interviewTextController;
    }

    public void executeCommand() {
        this.interviewTextController.addPaneForDragAndDrop();
    }
}
