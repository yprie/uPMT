package persistency.newSaveSystem.upgrades;

public abstract class SimpleUpgradeStrategy implements UpgradeStrategy {

    private int priorVersion;
    private int nextVersion;
    private String modelName;

    public SimpleUpgradeStrategy(String modelName, int priorVersion, int nextVersion) {
        this.modelName = modelName;
        this.priorVersion = priorVersion;
        this.nextVersion = nextVersion;
        //Only allow one by one upgrade
        if(this.priorVersion + 1 != nextVersion) {
            throw new IllegalArgumentException("Impossible to upgrade '"+ this.modelName +"' from version (" + this.priorVersion + ") to (" + this.nextVersion + ") with a SimpleUpgradeStrategy");
        }
    }

    @Override
    public int getPriorVersion() {
        return priorVersion;
    }

    @Override
    public int getNextVersion() {
        return nextVersion;
    }

}
