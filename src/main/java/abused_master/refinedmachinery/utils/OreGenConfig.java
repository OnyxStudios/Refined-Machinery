package abused_master.refinedmachinery.utils;

import abused_master.abusedlib.AbusedLib;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OreGenConfig {

    public static OreGenConfig INSTANCE = new OreGenConfig();
    private Map<String, OreGenEntry> entries = new HashMap<>();
    private File oreGenFile = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/refinedmachinery/oregen.json");

    public void initOreGen() {
        if(oreGenFile.exists()) {
            this.loadConfig();
            AbusedLib.LOGGER.info("Loaded Ore Generation Config for Refined Machinery");
        }else {
            addEntry("copper_ore", 9, 40, 64, true);
            addEntry("tin_ore", 9, 40, 64, true);
            addEntry("lead_ore", 9, 40, 50, true);
            addEntry("silver_ore", 9, 35, 40, true);
            addEntry("nickel_ore", 9, 40, 60, true);
            this.createConfig();
            AbusedLib.LOGGER.info("Created Ore Generation Config for Refined Machinery");
        }
    }

    public void addEntry(String name, int size, int count, int maxHeight, boolean generate) {
        this.entries.put(name, new OreGenEntry(size, count, maxHeight, generate));
    }

    public OreGenEntry getEntry(String name) {
        for (Map.Entry<String, OreGenEntry> entry : entries.entrySet()) {
            if(entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }

        return new OreGenEntry(0, 0, 0, true);
    }

    public void loadConfig() {
        JsonParser parser = new JsonParser();
        try {
            JsonObject config = parser.parse(new FileReader(oreGenFile)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : config.entrySet()) {
                String name = entry.getKey();
                JsonObject props = entry.getValue().getAsJsonObject();
                int size = props.get("size").getAsInt();
                int count = props.get("count").getAsInt();
                int maxHeight = props.get("maxHeight").getAsInt();
                boolean generate = props.get("generate").getAsBoolean();

                addEntry(name, size, count, maxHeight, generate);
            }
        } catch (FileNotFoundException e) {
            AbusedLib.LOGGER.error("Cannot find Ore Gen config!", e);
        }
    }

    public void createConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(oreGenFile)) {
            JsonObject config = new JsonObject();

            for (Map.Entry<String, OreGenEntry> entry : entries.entrySet()) {
                JsonObject oreProps = new JsonObject();
                oreProps.addProperty("size", entry.getValue().getSize());
                oreProps.addProperty("count", entry.getValue().getCount());
                oreProps.addProperty("maxHeight", entry.getValue().getMaxHeight());
                oreProps.addProperty("generate", entry.getValue().doesGenerate());

                config.add(entry.getKey(), oreProps);
            }

            gson.toJson(config, writer);
        } catch (IOException e) {
            AbusedLib.LOGGER.error("Something borked with creating oreGen", e);
        }
    }
}
