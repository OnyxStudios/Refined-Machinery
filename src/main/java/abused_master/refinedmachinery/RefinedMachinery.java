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
        configOptions.put("_comment1", "Refined Machinery Mod Config");
        configOptions.put("_comment2", "Disable or enable generation of the ores in the mod");
        configOptions.put("generateCopper", true);
        configOptions.put("generateTin", true);
        configOptions.put("generateLead", true);
        configOptions.put("generateSilver", true);
        configOptions.put("generateNickel", true);
        configOptions.put("_comment3", "The range in blocks that the pump will work in");
        configOptions.put("pumpRange", 32);

        return configOptions;
    }
}
