package components.modelisationSpace.justification.modelCommands;

import application.history.ModelUserActionCommand;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.Section;
import components.modelisationSpace.justification.models.Justification;


public class MoveDescripteme extends ModelUserActionCommand<Void, Void> {

    private boolean simpleMove = true;
    private Descripteme descripteme;
    private Justification source;
    private Justification target;
    private Descripteme targetDescripteme;
    private Section dragSection;

    private int sourceIndex;

    public MoveDescripteme(Descripteme d, Justification source, Justification target, Descripteme targetDescripteme, Section dragSection) {
        simpleMove = false;
        this.descripteme = d;
        this.source = source;
        this.target = target;
        this.targetDescripteme = targetDescripteme;
        this.dragSection = dragSection;
    }

    public MoveDescripteme(Descripteme d, Justification source, Justification target) {
        this.descripteme = d;
        this.source = source;
        this.target = target;
    }

    @Override
    public Void execute() {
        this.sourceIndex = target.indexOf(descripteme);
        this.source.removeDescripteme(descripteme);
        if(simpleMove)
            this.target.addDescripteme(descripteme);
        else
            this.target.addDescripteme(descripteme, target.indexOf(targetDescripteme) + (dragSection == Section.bottom ? 1 : 0));
        return null;
    }

    @Override
    public Void undo() {
        this.target.removeDescripteme(descripteme);
        this.source.addDescripteme(descripteme, sourceIndex);
        return null;
    }
}
