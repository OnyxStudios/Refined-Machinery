package dev.onyxstudios.refinedmachinery.utils;

import net.minecraft.util.IStringSerializable;

public enum WindTurbineType implements IStringSerializable {
    BASE,
    MIDDLE,
    TOP;

    @Override
    public String getString() {
        return toString().toLowerCase();
    }
}
