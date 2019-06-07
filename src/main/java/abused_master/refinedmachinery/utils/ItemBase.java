package abused_master.refinedmachinery.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;

public class ItemBase extends Item {
    private String name;

    public ItemBase(String name, ItemGroup tab) {
        super((new Settings()).group(tab));
        this.name = name;
    }

    public ItemBase(String name, Settings itemSettings) {
        super(itemSettings);
        this.name = name;
    }

    public Component getName() {
        return new TextComponent(this.name);
    }

    public Identifier getNameIdentifier(String modid) {
        return new Identifier(modid, this.name);
    }
}