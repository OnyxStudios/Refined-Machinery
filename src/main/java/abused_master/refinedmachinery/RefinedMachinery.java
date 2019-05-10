package abused_master.refinedmachinery;

import abused_master.abusedlib.utils.Config;
import abused_master.refinedmachinery.items.tools.ItemEnergizedSword;
import abused_master.refinedmachinery.registry.*;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RefinedMachinery implements ModInitializer {

    public static final String RESOURCES_TAG = "c";
    public static String MODID = "refinedmachinery";
    public static ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier(MODID, "refinedmachinery"), () -> new ItemStack(ModBlocks.ENERGY_FURNACE));

    public static Config config;
    public static File rmfolder = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/refinedmachinery");

    @Override
    public void onInitialize() {
        if(!rmfolder.exists()) {
            rmfolder.mkdir();
        }

        config = new Config("refinedmachinery/" + MODID, this.loadConfig());
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        WorldGenRegistry.generateOres();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntities.registerServerGUIs();
        PulverizerRecipes.INSTANCE.initRecipes();

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if(player.getMainHandStack().getItem() == ModItems.WRENCH) {
                player.getMainHandStack().getItem().useOnBlock(new ItemUsageContext(player, hand, hitResult));
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                ItemStack stack = player.getStackInHand(hand);
                int energyUsage = (int) (livingEntity.getHealth() * ModItems.ENERGIZED_SWORD.attackHeartCost);

                if (stack.isEmpty() || stack.getItem() != ModItems.ENERGIZED_SWORD) {
                    return ActionResult.PASS;
                }

                if (ModItems.ENERGIZED_SWORD.storage.getEnergyStored(stack) >= energyUsage) {
                    return ActionResult.PASS;
                }

                return ActionResult.SUCCESS;
            }else {
                return ActionResult.PASS;
            }
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
        configOptions.put("solarPanelMK3_Generation", 1024);
        configOptions.put("lavaGen_Generation", 100);
        configOptions.put("quarryUsagePerBlock", 500);
        configOptions.put("phaseCellStorage", 1000000);
        configOptions.put("grinderCostPerHeart", 50);
        configOptions.put("farmerCostPerBlock", 200);
        configOptions.put("copperTankStorage", 8000);
        configOptions.put("silverTankStorage", 16000);
        configOptions.put("steelTankStorage", 32000);
        configOptions.put("coalGen_Generation", 10);
        configOptions.put("disenchanter_cost", 2500);
        configOptions.put("useCottonResources", false);

        return configOptions;
    }
}
