package components.modelisationSpace.moment.appCommands;

import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import models.ConcreteCategory;
import models.Moment;
import models.RootMoment;
import models.SchemaCategory;
import models.Descripteme;

public class MomentCommandFactory {

    private RootMoment parent;
    private ModelisationSpaceHookNotifier hookNotifier;

    public MomentCommandFactory(ModelisationSpaceHookNotifier hookNotifier, RootMoment parent) {
        this.hookNotifier = hookNotifier;
        this.parent = parent;
    }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, int index) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m, index);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m);
    }


    public AddSiblingMomentCommand addSiblingCommand(Moment m, ConcreteCategory category) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m, category);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m, ConcreteCategory category, int index) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m, category, index);
    }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, SchemaCategory category, Moment parent) {
        return new AddSiblingMomentCommand(hookNotifier, this.parent, m, category, parent);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m, SchemaCategory category, Moment parent, int index) {
        return new AddSiblingMomentCommand(hookNotifier, this.parent, m, category, parent, index);
    }

    public MoveMomentCommand moveMomentCommand(Moment m, RootMoment originParent, int index){
        return new MoveMomentCommand(parent, originParent, m, index);
    }
    public MoveMomentCommand moveMomentCommand(Moment m, RootMoment originParent){
        return new MoveMomentCommand(parent, originParent, m);
    }
    public DeleteMomentCommand deleteCommand(Moment m) { return new DeleteMomentCommand(hookNotifier, parent, m, true); }
    public SetMomentTransCommand transitionCommand(Moment m) { return new SetMomentTransCommand(m); }
    public AddCommentCommand addCommentCommand(Moment m, String comment){ return new AddCommentCommand(m, comment);}

    public RootMoment getParentMoment(){
        return parent;
    }
    public ModelisationSpaceHookNotifier getHookNotifier() { return hookNotifier; }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, Descripteme descripteme) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m, descripteme);
    }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, Descripteme descripteme, int index) {
        return new AddSiblingMomentCommand(hookNotifier, parent, m, descripteme, index);
    }

    public ChangeColorMomentCommand colorCommand(Moment m, String color) {
        return new ChangeColorMomentCommand(m, color);
    }

    public MergeMomentCommand mergeMomentCommand(Moment destinationMoment, Moment sourceMoment, boolean userCommand) {
        return new MergeMomentCommand(hookNotifier, destinationMoment, sourceMoment, userCommand);
    }
}
