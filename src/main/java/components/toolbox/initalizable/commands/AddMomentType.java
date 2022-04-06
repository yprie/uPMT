package components.toolbox.initalizable.commands;

import application.history.ModelUserActionCommand;
import components.toolbox.initalizable.ToolBoxControllers;
import components.toolbox.initalizable.components.MomentTypeController;
import models.Moment;
import models.MomentType;

public class AddMomentType extends ModelUserActionCommand {
    ToolBoxControllers toolBoxControllers;
    Moment moment;
    MomentType momentType;

    public AddMomentType(ToolBoxControllers tbc, Moment m) {
        this.toolBoxControllers = tbc;
        this.moment = m;
    }

    @Override
    public Object execute() {
        /* On ajoute le mtc dans la Hbox de la tbc
        * On l'ajoute aussi dans le tableau de tbc */
        MomentTypeController mtc = new MomentTypeController(this.moment);
        this.toolBoxControllers.getContainerMomentsTypes().getChildren().add(MomentTypeController.createMomentTypeController(mtc));
        this.toolBoxControllers.getCurrentMomentsTypesList().add(this.moment);
        /* on crée le schemaMomentType dans l'arbre à gauche */
        this.toolBoxControllers.getMomentTypesFolder().addChild(mtc.getMomentType().getSchemaMomentType());

        this.momentType = mtc.getMomentType();
        return null;
    }

    @Override
    public Object undo() {
        /* On supprime le mtc de la Hbox de la tbc
         * On le supprime aussi du tableau de tbc */
        /* On supprime le schemaMomentType dans l'arbre à gauche */
        this.toolBoxControllers.getMomentTypesFolder().removeChild(this.momentType.getSchemaMomentType());
        this.toolBoxControllers.getContainerMomentsTypes().getChildren().remove(this.toolBoxControllers.getCurrentMomentsTypesList().indexOf(this.moment));
        this.toolBoxControllers.getCurrentMomentsTypesList().remove(this.moment);
        return null;
    }
}
