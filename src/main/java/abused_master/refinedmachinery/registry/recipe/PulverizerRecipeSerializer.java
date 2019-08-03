package abused_master.refinedmachinery.registry.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

public class PulverizerRecipeSerializer implements RecipeSerializer<PulverizerRecipe> {

    @Override
    public PulverizerRecipe read(Identifier identifier, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack randomOutput = ItemStack.EMPTY;
        int chance = 0;

        if(json.has("randomOutput")) {
            randomOutput = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "randomOutput"));
            chance = JsonHelper.getInt(JsonHelper.getObject(json, "randomOutput"), "chance");
        }
        ItemStack result = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));

        return new PulverizerRecipe(identifier, group, input, result, randomOutput, chance);
    }

    @Override
    public PulverizerRecipe read(Identifier identifier, PacketByteBuf buf) {
        return new PulverizerRecipe(identifier, buf.readString(), Ingredient.fromPacket(buf), buf.readItemStack(), buf.readItemStack(), buf.readInt());
    }

    @Override
    public void write(PacketByteBuf buf, PulverizerRecipe recipe) {
        buf.writeString(recipe.group);
        recipe.input.write(buf);
        buf.writeItemStack(recipe.result);
        buf.writeItemStack(recipe.randomOutput);
        buf.writeInt(recipe.randomOutputChance);
    }
}
