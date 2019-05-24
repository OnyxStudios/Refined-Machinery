package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.AbusedLib;
import abused_master.refinedmachinery.items.EnumResourceItems;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.util.Map;

public class PulverizerRecipes {

    public static PulverizerRecipes INSTANCE = instance();
    private final Map<Object, PulverizerRecipe> recipesMap = Maps.newHashMap();
    public static File recipesFile = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/refinedmachinery/pulverizer.json");

    private static PulverizerRecipes instance() {
        return new PulverizerRecipes();
    }

    public void initRecipes() {
        if(recipesFile.exists()) {
            loadRecipesConfig();
        }else {
            //Default recipes
            registerRecipe(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), 15);
            registerRecipe(new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.GRAVEL), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), new ItemStack(Items.FLINT), 15);
            registerRecipe(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND, 2), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.SMOOTH_SANDSTONE), new ItemStack(Blocks.SAND, 2), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.CHISELED_SANDSTONE), new ItemStack(Blocks.SAND, 2), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Items.CLAY_BALL, 4), ItemStack.EMPTY, 0);

            registerRecipe(ItemTags.COALS, new ItemStack(EnumResourceItems.COAL_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Items.BONE), new ItemStack(Items.BONE_MEAL, 6), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 4), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.MAGMA_BLOCK), new ItemStack(Items.MAGMA_CREAM, 4), ItemStack.EMPTY, 0);

            registerRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(EnumResourceItems.IRON_DUST.getItemIngot(), 2), new ItemStack(EnumResourceItems.NICKEL_DUST.getItemIngot()), 10);
            registerRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(EnumResourceItems.GOLD_DUST.getItemIngot(), 2), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.COPPER_ORE, new ItemStack(EnumResourceItems.COPPER_DUST.getItemIngot(), 2), new ItemStack(EnumResourceItems.GOLD_DUST.getItemIngot()), 10);
            registerRecipe(RMTags.TIN_ORE, new ItemStack(EnumResourceItems.TIN_DUST.getItemIngot(), 2), new ItemStack(EnumResourceItems.IRON_DUST.getItemIngot()), 10);
            registerRecipe(RMTags.SILVER_ORE, new ItemStack(EnumResourceItems.SILVER_DUST.getItemIngot(), 2), new ItemStack(EnumResourceItems.LEAD_DUST.getItemIngot()), 10);
            registerRecipe(RMTags.LEAD_ORE, new ItemStack(EnumResourceItems.LEAD_DUST.getItemIngot(), 2), new ItemStack(EnumResourceItems.SILVER_DUST.getItemIngot()), 10);
            registerRecipe(RMTags.NICKEL_ORE, new ItemStack(EnumResourceItems.NICKEL_DUST.getItemIngot(), 2), ItemStack.EMPTY, 10);

            registerRecipe(new ItemStack(Items.DIAMOND), new ItemStack(EnumResourceItems.DIAMOND_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.COPPER_INGOT, new ItemStack(EnumResourceItems.COPPER_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.TIN_INGOT, new ItemStack(EnumResourceItems.TIN_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.LEAD_INGOT, new ItemStack(EnumResourceItems.LEAD_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.SILVER_INGOT, new ItemStack(EnumResourceItems.SILVER_DUST.getItemIngot()), ItemStack.EMPTY, 0);
            registerRecipe(RMTags.NICKEL_INGOT, new ItemStack(EnumResourceItems.NICKEL_DUST.getItemIngot()), ItemStack.EMPTY, 0);

            registerRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL, 3), new ItemStack(EnumResourceItems.COAL_DUST.getItemIngot()), 25);
            registerRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.LAPIS_LAZULI, 8), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE, 6), ItemStack.EMPTY, 0);
            registerRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(EnumResourceItems.DIAMOND_DUST.getItemIngot(), 2), new ItemStack(Items.DIAMOND), 10);
            registerRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD, 2), new ItemStack(Items.EMERALD), 10);
            registerRecipe(new ItemStack(Blocks.NETHER_QUARTZ_ORE), new ItemStack(Items.QUARTZ, 6), ItemStack.EMPTY, 0);

            registerRecipe(ItemTags.WOOL, new ItemStack(Items.STRING, 4), ItemStack.EMPTY, 0);

            generateRecipesConfig();
        }
    }

    public void generateRecipesConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(recipesFile)){
            JsonObject recipes = new JsonObject();

            for (Map.Entry<Object, PulverizerRecipe> entry : this.recipesMap.entrySet()) {
                String input = entry.getKey() instanceof ItemStack ? Registry.ITEM.getId(((ItemStack) entry.getKey()).getItem()).toString() : "#" + ((Tag) entry.getKey()).getId().toString();
                JsonObject entryObj = new JsonObject();
                entryObj.addProperty("output", Registry.ITEM.getId(entry.getValue().getOutput().getItem()).toString());
                entryObj.addProperty("outputAmount", entry.getValue().getOutput().getAmount());
                entryObj.addProperty("randomOutput", Registry.ITEM.getId(entry.getValue().getRandomDrop().getItem()).toString());
                entryObj.addProperty("randomOutputAmount", entry.getValue().getRandomDrop().getAmount());
                entryObj.addProperty("randomOutputChance", entry.getValue().getPercentageDrop());

                recipes.add(input, entryObj);
            }

            gson.toJson(recipes, writer);
            AbusedLib.LOGGER.info("Created Pulverizer Recipes!");
        } catch (IOException e) {
            AbusedLib.LOGGER.warn("Pulverizer recipe creation failed!", e);
        }
    }

    public void loadRecipesConfig() {
        JsonParser parser = new JsonParser();
        try {
            JsonObject recipes = parser.parse(new FileReader(recipesFile)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : recipes.entrySet()) {
                JsonObject props = entry.getValue().getAsJsonObject();
                Object input = entry.getKey().startsWith("#") ? TagRegistry.item(new Identifier(entry.getKey().replace("#", ""))) : new ItemStack(Registry.ITEM.get(new Identifier(entry.getKey())));
                ItemStack output = new ItemStack(Registry.ITEM.get(new Identifier(props.get("output").getAsString())), props.get("outputAmount").getAsInt());
                ItemStack randomOutput = new ItemStack(Registry.ITEM.get(new Identifier(props.get("randomOutput").getAsString())), props.get("randomOutputAmount").getAsInt());
                int randomOutputChance = props.get("randomOutputChance").getAsInt();

                registerRecipe(input, output, randomOutput, randomOutputChance);
            }

            AbusedLib.LOGGER.info("Loaded Pulverizer Recipes!");
        } catch (FileNotFoundException e) {
            AbusedLib.LOGGER.warn("You done borked something with your config!", e);
        }
    }

    public void registerRecipe(Object input, ItemStack output, ItemStack randomDrop, int percentageDrop) {
        recipesMap.put(input, new PulverizerRecipe(output, randomDrop, percentageDrop));
    }

    public PulverizerRecipe getOutputRecipe(ItemStack input) {
        for (Object key : this.recipesMap.keySet()) {
            if(key instanceof ItemStack && input.getItem() == ((ItemStack) key).getItem()) {
                return this.recipesMap.get(key);
            }else if(key instanceof Tag && ((Tag) key).contains(input.getItem())) {
                return this.recipesMap.get(key);
            }
        }

        return null;
    }

    public Map<Object, PulverizerRecipe> getRecipes() {
        return recipesMap;
    }

    public class PulverizerRecipe {

        private ItemStack output, randomDrop;
        private int percentageDrop;

        public PulverizerRecipe(ItemStack output, ItemStack randomDrop, int percentageDrop) {
            this.output = output;
            this.randomDrop = randomDrop;
            this.percentageDrop = percentageDrop;
        }

        public ItemStack getOutput() {
            return output;
        }

        public ItemStack getRandomDrop() {
            return randomDrop;
        }

        public int getOutputAmount() {
            return output.getAmount();
        }

        public int getRandomDropAmoumt() {
            return randomDrop.getAmount();
        }

        public int getPercentageDrop() {
            return percentageDrop;
        }

        public PulverizerRecipe get() {
            return this;
        }
    }
}
