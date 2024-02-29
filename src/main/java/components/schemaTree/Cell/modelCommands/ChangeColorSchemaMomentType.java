package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import models.SchemaCategory;
import models.SchemaMomentType;

public class ChangeColorSchemaMomentType extends ModelUserActionCommand{

        private SchemaMomentType momentType;
        private String oldColor;
        private String newColor;

        public ChangeColorSchemaMomentType(SchemaMomentType momentType, String newColor, String oldColor) {
            this.momentType = momentType;
            this.newColor = newColor;
            this.oldColor = oldColor;
        }

        @Override
        public Object execute() {
            momentType.setColor(newColor);
            return null;
        }

        @Override
        public Object undo() {
            momentType.setColor(oldColor);
            return null;
        }

    }
