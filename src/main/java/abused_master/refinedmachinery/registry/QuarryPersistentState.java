package abused_master.refinedmachinery.registry;

import abused_master.refinedmachinery.tiles.machine.BlockEntityQuarry;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuarryPersistentState extends PersistentState {

    public static final String ID = "QUARRY_SAVE_STATE";
    public Map<BlockPos, List<BlockPos>> quarryPositions = Maps.newHashMap();

    public QuarryPersistentState() {
        super(ID);
    }

    public void putQuarryData(BlockPos quarryPos, List<BlockPos> miningPositions) {
        if(hasQuarryData(quarryPos)) return;
        this.quarryPositions.put(quarryPos, miningPositions);
    }

    public void putQuarryData(BlockPos quarryPos, BlockPos firstCorner, BlockPos secondCorner) {
        Iterable<BlockPos> blocksInQuarry = BlockPos.iterate(secondCorner, firstCorner);
        List<BlockPos> miningPositionsList = BlockEntityQuarry.listBlocksInIterable(blocksInQuarry);
        putQuarryData(quarryPos, miningPositionsList);
    }

    public boolean hasQuarryData(BlockPos quarryPos) {
        return this.quarryPositions.containsKey(quarryPos);
    }

    public void removeQuarryData(BlockPos quarryPos) {
        this.quarryPositions.remove(quarryPos);
    }

    public BlockPos getNextMiningPos(BlockPos quarryPos) {
        return !getMiningPositions(quarryPos).isEmpty() ? getMiningPositions(quarryPos).get(0) : null;
    }

    public List<BlockPos> getMiningPositions(BlockPos quarryPos) {
        return quarryPositions.getOrDefault(quarryPos, new ArrayList<>());
    }

    public static QuarryPersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(QuarryPersistentState::new, ID);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag worldQuarryData = tag.getList("quarryData", NbtType.COMPOUND);

        for (Tag dataTag : worldQuarryData) {
            CompoundTag quarryData = (CompoundTag) dataTag;
            ListTag miningPositions = quarryData.getList("miningPositions", NbtType.COMPOUND);

            BlockPos quarryPos = TagHelper.deserializeBlockPos(quarryData.getCompound("quarryPos"));
            List<BlockPos> miningPosList = new ArrayList<>();

            for (Tag posTag : miningPositions) {
                miningPosList.add(TagHelper.deserializeBlockPos((CompoundTag) posTag));
            }

            quarryPositions.put(quarryPos, miningPosList);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag worldQuarryData = new ListTag();

        for (Map.Entry<BlockPos, List<BlockPos>> entry : quarryPositions.entrySet()) {
            CompoundTag quarryData = new CompoundTag();
            ListTag miningPositions = new ListTag();
            quarryData.put("quarryPos", TagHelper.serializeBlockPos(entry.getKey()));

            for (BlockPos pos : entry.getValue()) {
                miningPositions.add(TagHelper.serializeBlockPos(pos));
            }

            quarryData.put("miningPositions", miningPositions);
            worldQuarryData.add(quarryData);
        }

        tag.put("quarryData", worldQuarryData);

        return tag;
    }
}
