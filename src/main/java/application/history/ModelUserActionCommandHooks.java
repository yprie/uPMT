package application.history;

import java.util.HashMap;

public class ModelUserActionCommandHooks {

    public enum HookMoment { BeforeExecute, AfterExecute, BeforeUndo, AfterUndo };
    private HashMap<HookMoment, Runnable> hooks;

    public ModelUserActionCommandHooks() {
        this.hooks = new HashMap<>();
    }

    public void setHook(HookMoment moment, Runnable runnable) {
        hooks.put(moment, runnable);
    }

    public void runHook(HookMoment moment) {
        if(hooks.containsKey(moment))
            hooks.get(moment).run();
    }

}
