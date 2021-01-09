package dev.onyxstudios.refinedmachinery.client.container;

import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineContainer extends Container {

    protected PlayerInventory playerInventory;
    protected TileEntityConfigurable tile;
    protected List<Integer> playerSlots = new ArrayList<>();
    public boolean isSettingsOpen = false;

    public BaseMachineContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, TileEntityConfigurable tile) {
        super(type, id);
        this.playerInventory = playerInventory;
        this.tile = tile;

        //Add Player Inventory Slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                playerSlots.add(j + i * 9 + 9);
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
            playerSlots.add(k);
        }
    }

    public void togglePlayerSlots() {
        for (int slot : playerSlots) {
            moveSlot(this.getSlot(slot));
        }
    }

    public void moveSlot(Slot slot) {
        try {
            Field xPos = Slot.class.getField("xPos");
            Field yPos = Slot.class.getField("yPos");

            xPos.setAccessible(true);
            yPos.setAccessible(true);

            xPos.set(slot, slot.xPos + (isSettingsOpen ? 999 : -999));
            yPos.set(slot, slot.yPos + (isSettingsOpen ? 999 : -999));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public TileEntityConfigurable getTileEntity() {
        return this.tile;
    }
}
