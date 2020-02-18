package components.modelisationSpace.justification.appCommands;

import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.Section;
import components.modelisationSpace.justification.models.Justification;

public class JustificationCommandFactory {

    private Justification justification;

    public JustificationCommandFactory(Justification j) {
        this.justification = j;
    }

    public AddDescriptemeCommand addDescripteme(Descripteme d) { return new AddDescriptemeCommand(justification, d); }
    public AddDescriptemeCommand addDescripteme(Descripteme d, int index) { return new AddDescriptemeCommand(justification, d, index); }
    public RemoveDescriptemeCommand removeDescripteme(Descripteme d) { return new RemoveDescriptemeCommand(justification, d); }
    public DuplicateDescriptemeCommand duplicateDescripteme(Descripteme d) { return new DuplicateDescriptemeCommand(justification, d); }

    public MoveDescriptemeCommand moveDescripteme(Descripteme d, JustificationCommandFactory targetFactory) { return new MoveDescriptemeCommand(d, justification, targetFactory.getJustification()); }
    public MoveDescriptemeCommand moveDescripteme(Descripteme d, JustificationCommandFactory targetFactory, Descripteme targetDescripteme, Section dragSection) { return new MoveDescriptemeCommand(d, justification, targetFactory.getJustification(), targetDescripteme, dragSection); }

    private Justification getJustification() {
        return justification;
    }

}
