package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.Section;
import components.modelisationSpace.justification.modelCommands.MoveDescripteme;
import components.modelisationSpace.justification.models.Justification;
import utils.command.Executable;

public class MoveDescriptemeCommand implements Executable<Void> {

    private Descripteme descripteme;
    private Justification source;
    private Justification target;
    private Descripteme targetDescripteme;
    private Section dragSection;
    private boolean simpleDrag = true;

    public MoveDescriptemeCommand(Descripteme d, Justification source, Justification target, Descripteme targetDescripteme, Section dragSection) {
        simpleDrag = false;
        this.descripteme = d;
        this.source = source;
        this.target = target;
        this.targetDescripteme = targetDescripteme;
        this.dragSection = dragSection;
    }

    public MoveDescriptemeCommand(Descripteme d, Justification source, Justification target) {
        this.descripteme = d;
        this.source = source;
        this.target = target;
    }

    @Override
    public Void execute() {
        if(simpleDrag){
            HistoryManager.addCommand(new MoveDescripteme(descripteme, source, target), true);
        }
        else {
            HistoryManager.addCommand(new MoveDescripteme(descripteme, source, target, targetDescripteme, dragSection), true);
        }
        return null;
    }

}
