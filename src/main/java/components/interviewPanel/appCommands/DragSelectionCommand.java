package components.interviewPanel.appCommands;

import components.interviewPanel.Controllers.InterviewTextController;
import components.interviewPanel.Controllers.RichTextAreaController;
import javafx.scene.control.IndexRange;
import utils.command.Executable;

public class DragSelectionCommand implements Executable<Void> {
    InterviewTextController interviewTextController;
    RichTextAreaController richTextAreaController;
    IndexRange indexRange;

    DragSelectionCommand(InterviewTextController interviewTextController, RichTextAreaController richTextAreaController, IndexRange indexRange) {
        this.interviewTextController = interviewTextController;
        this.richTextAreaController = richTextAreaController;
        this.indexRange = indexRange;
    }

    @Override
    public Void execute() {
        richTextAreaController.select(indexRange);
        interviewTextController.addPaneForDragAndDrop();
        return null;
    }
}
