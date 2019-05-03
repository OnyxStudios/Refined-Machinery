package abused_master.refinedmachinery;

import abused_master.abusedlib.utils.Config;
import abused_master.refinedmachinery.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class RefinedMachinery implements ModInitializer {

    public static final String RESOURCES_TAG = "fabric";
    public static String MODID = "refinedmachinery";
    public static ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier(MODID, "refinedmachinery"), () -> new ItemStack(ModBlocks.ENERGY_FURNACE));

    public static Config config;

    @Override
    public void onInitialize() {
        config = new Config(MODID, this.loadConfig());
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        WorldGenRegistry.generateOres();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntities.registerServerGUIs();
        ModRecipes.registerRecipes();

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if(player.getMainHandStack().getItem() == ModItems.WRENCH) {
                player.getMainHandStack().getItem().useOnBlock(new ItemUsageContext(player, hand, hitResult));
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }

    public Map<String, Object> loadConfig() {
        Map<String, Object> configOptions = new HashMap<>();
        configOptions.put("generateCopper", true);
        configOptions.put("generateTin", true);
        configOptions.put("generateLead", true);
        configOptions.put("generateSilver", true);
        configOptions.put("generateNickel", true);
        configOptions.put("pumpRange", 32);
        configOptions.put("pumpCostPerBlock", 250);
        configOptions.put("solarPanelMK1_Generation", 16);
        configOptions.put("solarPanelMK2_Generation", 256);
        configOptions.put("solarPanelMK2_Generation", 1024);
        configOptions.put("lavaGen_Generation", 100);
        configOptions.put("quarryUsagePerBlock", 500);
        configOptions.put("phaseCellStorage", 1000000);
        configOptions.put("grinderCostPerHeart", 50);
        configOptions.put("farmerCostPerBlock", 200);
        configOptions.put("copperTankStorage", 8000);
        configOptions.put("silverTankStorage", 16000);
        configOptions.put("steelTankStorage", 32000);

        return configOptions;
    }
}
