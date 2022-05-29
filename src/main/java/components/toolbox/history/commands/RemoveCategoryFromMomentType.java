package components.toolbox.history.commands;

import application.history.ModelUserActionCommand;
import models.SchemaCategory;
import models.SchemaMomentType;

public class RemoveCategoryFromMomentType extends ModelUserActionCommand {
    SchemaMomentType smt;
    SchemaCategory sc;

    public RemoveCategoryFromMomentType(SchemaMomentType smt, SchemaCategory sc) {
        this.smt = smt;
        this.sc = sc;
    }


    @Override
    public Object execute() {
        smt.removeCategory(sc);
        return null;
    }

    @Override
    public Object undo() {
        smt.addCategory(sc, -1);
        return null;
    }
}
