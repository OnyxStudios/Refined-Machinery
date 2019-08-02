package abused_master.refinedmachinery.tiles.transport;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.ModPackets;
import abused_master.refinedmachinery.utils.EnergyHelper;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlockEntityWirelessController extends BlockEntityBase implements IEnergyHandler, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public final Set<BlockPos> tilePositions = new HashSet<>();
    public int sendPerTick = 250;

    public BlockEntityWirelessController() {
        super(ModBlockEntities.ENERGY_CRYSTAL);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        storage.fromTag(nbt);

        if(nbt.containsKey("tilePositions")) {
            tilePositions.clear();
            ListTag tags = nbt.getList("tilePositions", NbtType.COMPOUND);
            for (Tag tag : tags) {
                tilePositions.add(TagHelper.deserializeBlockPos((CompoundTag) tag));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        storage.toTag(nbt);

        if(tilePositions.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos1 : tilePositions) {
                tags.add(TagHelper.serializeBlockPos(pos1));
            }

            nbt.put("tilePositions", tags);
        }

        return nbt;
    }

    @Override
    public void tick() {
        if(storage.getEnergyStored() >= sendPerTick && tilePositions.size() > 0) {
            sendEnergy();
        }
    }

    public void sendEnergy() {
        for (Iterator<BlockPos> it = tilePositions.iterator(); it.hasNext();) {
            BlockPos blockPos = it.next();
            if(blockPos == null || !(world.getBlockEntity(blockPos) instanceof IEnergyHandler) || !((BlockComponentProvider) world.getBlockState(blockPos).getBlock()).hasComponent(world, blockPos, DefaultTypes.CARDINAL_ENERGY, null)) {
                it.remove();
                this.updateEntity();
                continue;
            }

            EnergyHelper.sendEnergy(getPos(), storage, world, blockPos, sendPerTick);
            this.updateEntity();
        }
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if(!world.isClient) {
            ItemHelper.handleControllerLink(world, pos, player, tag);

            for (PlayerEntity worldPlayer : world.getPlayers()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBlockPos(pos);
                buf.writeCompoundTag(tag);
                ((ServerPlayerEntity) worldPlayer).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_LINK_ENERGY, buf));
            }
        }
    }
}
