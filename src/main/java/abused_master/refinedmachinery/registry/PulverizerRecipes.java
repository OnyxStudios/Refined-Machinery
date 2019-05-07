package abused_master.refinedmachinery.registry;

import abused_master.refinedmachinery.items.EnumResourceItems;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;

import java.util.Map;

public class PulverizerRecipes {

    public static PulverizerRecipes INSTANCE = instance();
    private final Map<Object, PulverizerRecipe> recipesMap = Maps.newHashMap();

    private static PulverizerRecipes instance() {
        return new PulverizerRecipes();
    }

    public PulverizerRecipes() {
        //Default recipes
        registerRecipe(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE), 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), 1, new ItemStack(Blocks.SAND), 1, 15);
        registerRecipe(new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.GRAVEL), 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), 1, new ItemStack(Items.FLINT), 1, 15);
        registerRecipe(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND), 2, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.SMOOTH_SANDSTONE), new ItemStack(Blocks.SAND), 2, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.CHISELED_SANDSTONE), new ItemStack(Blocks.SAND), 2, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Items.CLAY_BALL), 4, ItemStack.EMPTY, 0, 0);

        registerRecipe(ItemTags.COALS, new ItemStack(EnumResourceItems.COAL_DUST.getItemIngot()), 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Items.BONE), new ItemStack(Items.BONE_MEAL), 6, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST), 4, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER), 4, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.MAGMA_BLOCK), new ItemStack(Items.MAGMA_CREAM), 4, ItemStack.EMPTY, 0, 0);

        registerRecipe(new ItemStack(Blocks.IRON_ORE), RMTags.IRON_DUST, 2, RMTags.NICKEL_DUST, 1, 10);
        registerRecipe(new ItemStack(Blocks.GOLD_ORE), RMTags.GOLD_DUST, 2, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.COPPER_ORE, RMTags.COPPER_DUST, 2, RMTags.GOLD_DUST, 1, 10);
        registerRecipe(RMTags.TIN_ORE, RMTags.TIN_DUST, 2, RMTags.IRON_DUST, 1, 10);
        registerRecipe(RMTags.SILVER_ORE, RMTags.SILVER_DUST, 2, RMTags.LEAD_DUST, 1, 10);
        registerRecipe(RMTags.LEAD_ORE, RMTags.LEAD_DUST, 2, RMTags.SILVER_DUST, 1, 10);
        registerRecipe(RMTags.NICKEL_ORE, RMTags.NICKEL_DUST, 2, ItemStack.EMPTY, 1, 10);

        registerRecipe(new ItemStack(Items.DIAMOND), RMTags.DIAMOND_DUST, 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.COPPER_INGOT, RMTags.COPPER_DUST, 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.TIN_INGOT, RMTags.TIN_DUST, 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.LEAD_INGOT, RMTags.LEAD_DUST, 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.SILVER_INGOT, RMTags.SILVER_DUST, 1, ItemStack.EMPTY, 0, 0);
        registerRecipe(RMTags.NICKEL_INGOT, RMTags.NICKEL_DUST, 1, ItemStack.EMPTY, 0, 0);

        registerRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL), 3, RMTags.COAL_DUST, 1, 25);
        registerRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.LAPIS_LAZULI), 8, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE), 6, ItemStack.EMPTY, 0, 0);
        registerRecipe(new ItemStack(Blocks.DIAMOND_ORE), RMTags.DIAMOND_DUST, 2, new ItemStack(Items.DIAMOND), 1, 10);
        registerRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD), 2, new ItemStack(Items.EMERALD), 1, 10);
        registerRecipe(new ItemStack(Blocks.NETHER_QUARTZ_ORE), new ItemStack(Items.QUARTZ), 6, ItemStack.EMPTY, 0, 0);

        registerRecipe(ItemTags.WOOL, new ItemStack(Items.STRING), 4, ItemStack.EMPTY, 0, 0);
    }

    public void registerRecipe(Object input, Object output, int outputAmount, Object randomDrop, int randomDropAmount, int percentageDrop) {
        recipesMap.put(input, new PulverizerRecipe(output, outputAmount, randomDrop, randomDropAmount, percentageDrop));
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

    public class PulverizerRecipe {

        private ItemStack output, randomDrop;
        private int outputAmount, randomDropAmoumt, percentageDrop;

        public PulverizerRecipe(Object output, int outputAmount, Object randomDrop, int randomDropAmount, int percentageDrop) {
            this.output = output instanceof ItemStack ? (ItemStack) output : (((Tag) output).values().toArray()[0] instanceof Item ? new ItemStack((Item) ((Tag) output).values().toArray()[0]) : new ItemStack((Block) ((Tag) output).values().toArray()[0]));
            this.randomDrop = randomDrop instanceof ItemStack ? (ItemStack) randomDrop : (((Tag) randomDrop).values().toArray()[0] instanceof Item ? new ItemStack((Item) ((Tag) randomDrop).values().toArray()[0]) : new ItemStack((Block) ((Tag) randomDrop).values().toArray()[0]));
            this.percentageDrop = percentageDrop;
            this.outputAmount = outputAmount;
            this.randomDropAmoumt = randomDropAmount;
        }

        public ItemStack getOutput() {
            return output;
        }

        public ItemStack getRandomDrop() {
            return randomDrop;
        }

        public int getOutputAmount() {
            return outputAmount;
        }

        public int getRandomDropAmoumt() {
            return randomDropAmoumt;
        }

        public int getPercentageDrop() {
            return percentageDrop;
        }

        public PulverizerRecipe get() {
            return this;
        }
    }
}
