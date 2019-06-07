package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.items.*;
import abused_master.refinedmachinery.items.tools.ItemEnergizedSword;
import abused_master.refinedmachinery.items.tools.ItemRoboticWings;
import abused_master.refinedmachinery.utils.ItemBase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static ItemQuarryRecorder RECORDER = new ItemQuarryRecorder();
    public static ItemLinker LINKER = new ItemLinker();
    public static ItemEnergizedSword ENERGIZED_SWORD = new ItemEnergizedSword();
    public static ItemSteelIngot STEEL_INGOT = new ItemSteelIngot();
    public static ItemWrench WRENCH = new ItemWrench();
    public static ItemRoboticWings ROBOTIC_WINGS = new ItemRoboticWings();
    public static ItemBlockExchanger EXCHANGER = new ItemBlockExchanger();

    public static void registerItems() {
        registerItem(RefinedMachinery.MODID, RECORDER);
        registerItem(RefinedMachinery.MODID, LINKER);
        registerItem(RefinedMachinery.MODID, STEEL_INGOT);
        registerItem(RefinedMachinery.MODID, WRENCH);
        registerItem(RefinedMachinery.MODID, EXCHANGER);

        RegistryHelper.registerItem(new Identifier(RefinedMachinery.MODID, "energized_sword"), ENERGIZED_SWORD);
        RegistryHelper.registerItem(new Identifier(RefinedMachinery.MODID, "robotic_wings"), ROBOTIC_WINGS);

        for (EnumResourceItems item : EnumResourceItems.values()) {
            RegistryHelper.registerItem(RefinedMachinery.MODID, item.getItemIngot());
        }
    }

    private static void registerItem(String modid, ItemBase item) {
        Registry.register(Registry.ITEM, item.getNameIdentifier(modid), item);
    }
}
