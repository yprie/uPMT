package persistency.newSaveSystem.serialization;

import persistency.newSaveSystem.upgrades.UpgradeStrategy;

import java.util.HashMap;

public abstract class Serializable<ModelType> {

    protected ObjectSerializer serializer;
    private int version;
    private String name;
    private int serializationId;


    private HashMap<Integer, UpgradeStrategy> upgrade_strategies;

    //Reading initialization
    public Serializable(ObjectSerializer serializer) {
        this.serializer = serializer;
        this.upgrade_strategies = new HashMap<>();
        readHeader();
        read();

        addStrategies();
        upgrade();
    }

    //writing initialization
    public Serializable(String modelName, int version, Object modelReference) {
        this.upgrade_strategies = new HashMap<>();
        this.name = modelName;
        this.version = version;
        this.serializationId = System.identityHashCode(modelReference);
    }

    public final int getVersion() {
        return version;
    };
    public final String getName() {
        return name;
    }
    public final int getSerializationId() { return serializationId; }

    public void addUpgradingStrategy(UpgradeStrategy strategy) throws IllegalArgumentException {
        if (upgrade_strategies.containsKey(strategy.getPriorVersion()))
            throw new IllegalArgumentException("An existing upgrage for " + name + " from version (" + version + ") already exists");
        upgrade_strategies.put(strategy.getPriorVersion(), strategy);
    }

    protected abstract void addStrategies();

    public void upgrade() {
        UpgradeStrategy strategy = upgrade_strategies.get(version);
        if(strategy != null)
            upgrade_strategies.get(version).upgrade(serializer);
    }

    protected abstract void read();
    protected void readHeader() {
        this.name = serializer.getString("@model");
        this.version = serializer.getInt("@version");
        this.serializationId = serializer.getInt("@id");
    }

    protected abstract void write(ObjectSerializer serializer);

    public void save(ObjectSerializer serializer) {
        serializer.writeInt("@id", getSerializationId());
        serializer.writeString("@model", name);
        serializer.writeInt("@version", version);
        write(serializer);
    }

    public void saveReferenced(ObjectSerializer serializer) {
        serializer.writeInt("@id", getSerializationId());
        serializer.writeString("@model", name);
    }

    public ModelType convertToModel() {
        ModelType m;
        try {
            m = (ModelType)serializer.getModelsPool().get(serializationId);
            return m;
        }
        catch(IllegalArgumentException e) {
            m =  createModel();
            serializer.getModelsPool().add(serializationId, m);
            return m;
        }
    }

    protected abstract ModelType createModel();
}
