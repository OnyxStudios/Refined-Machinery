package dev.onyxstudios.refinedmachinery.network;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPackets {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RefinedMachinery.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        INSTANCE.registerMessage(0, UpdateRedstoneMessage.class, UpdateRedstoneMessage::encode, UpdateRedstoneMessage::decode, UpdateRedstoneMessage::handleMessage);
        INSTANCE.registerMessage(1, OpenSettingsMessage.class, OpenSettingsMessage::encode, OpenSettingsMessage::decode, OpenSettingsMessage::handleMessage);
        INSTANCE.registerMessage(2, ConfigureMachineMessage.class, ConfigureMachineMessage::encode, ConfigureMachineMessage::decode, ConfigureMachineMessage::handleMessage);
    }
}
