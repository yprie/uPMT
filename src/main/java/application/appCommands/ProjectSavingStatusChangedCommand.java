package application.appCommands;

import application.history.HistoryManager;
import application.UPMTApp;

import java.util.UUID;

public class ProjectSavingStatusChangedCommand extends ApplicationCommand<Void> {

    private String unsavedProjectSymbol = " * ";

    public ProjectSavingStatusChangedCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        //If the last saved command is the current command, then only remove * from title
        String currentTitle = upmtApp.getPrimaryStage().getTitle();
        UUID currentCommandId = HistoryManager.getCurrentCommandId();
        UUID lastSavedCommandId = upmtApp.getLastSavedCommandId();
        if(currentCommandId != null ){
            if (lastSavedCommandId == null){
                /* Correction : on ajoute '*' seulement si '*' n'est pas déjà présent dans le titre.
                 Patch : add '*' only if '*' is not already in the title. */
                if (currentTitle.charAt(currentTitle.length() - 2) != '*') {
                    upmtApp.getPrimaryStage().setTitle(currentTitle + unsavedProjectSymbol);
                }
            }else {
                if (HistoryManager.getCurrentCommandId().equals(lastSavedCommandId)) {
                    if (currentTitle.endsWith(unsavedProjectSymbol)) {
                        upmtApp.getPrimaryStage().setTitle(currentTitle.substring(0, currentTitle.length() - unsavedProjectSymbol.length()));
                    }
                } else if (!currentTitle.endsWith(unsavedProjectSymbol))
                    upmtApp.getPrimaryStage().setTitle(currentTitle + unsavedProjectSymbol);
            }
        }
        return null;
    }

}
