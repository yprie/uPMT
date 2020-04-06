package components.templateSpace.emptyTemplateMoment;

import application.configuration.Configuration;
import models.Moment;
import models.TemplateMoment;

public class EmptyTemplateMoment extends TemplateMoment {

    public EmptyTemplateMoment() {
        super(Configuration.langBundle.getString("new_moment"));
    }

    @Override
    public Moment createConcreteMoment() {
        return new Moment(nameProperty().getName());
    }
}
