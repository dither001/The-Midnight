package midnight.data.models.modelgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class InheritingModelGen implements IModelGen {
    private final ResourceLocation parent;
    private final List<Pair<String, String>> textureRef = new ArrayList<>();

    public InheritingModelGen(ResourceLocation parent) {
        this.parent = parent;
    }

    public InheritingModelGen(String parent) {
        this.parent = new ResourceLocation(parent);
    }

    public InheritingModelGen texture(String reference, String newReference) {
        textureRef.add(Pair.of(reference, newReference));
        return this;
    }

    public InheritingModelGen texture(String reference, ResourceLocation id) {
        textureRef.add(Pair.of(reference, id.toString()));
        return this;
    }

    @Override
    public JsonElement makeJson(ResourceLocation name) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", parent.toString().replace("{{name}}", name.toString()));
        if (!textureRef.isEmpty()) {
            JsonObject textures = new JsonObject();
            for (Pair<String, String> ref : textureRef) {
                textures.addProperty(
                        ref.getFirst().replace("{{name}}", name.toString()),
                        ref.getSecond().replace("{{name}}", name.toString())
                );
            }
            root.add("textures", textures);
        }
        return root;
    }

    public static InheritingModelGen inherit() {
        return new InheritingModelGen("{{name}}");
    }

    public static InheritingModelGen inherit(String parent) {
        return new InheritingModelGen(parent);
    }

    public static InheritingModelGen cubeAll(String texture) {
        return new InheritingModelGen("block/cube_all")
                .texture("all", texture);
    }

    public static InheritingModelGen cubeMirroredAll(String texture) {
        return new InheritingModelGen("block/cube_mirrored_all")
                .texture("all", texture);
    }

    public static InheritingModelGen cubeColumn(String end, String side) {
        return new InheritingModelGen("block/cube_column")
                   .texture("end", end)
                   .texture("side", side);
    }

    public static InheritingModelGen cubeColumnHoriz(String end, String side) {
        return new InheritingModelGen("block/cube_column_horizontal")
                .texture("end", end)
                .texture("side", side);
    }

    public static InheritingModelGen cubeBottomTop(String bottom, String top, String side) {
        return new InheritingModelGen("block/cube_bottom_top")
                .texture("bottom", bottom)
                .texture("top", top)
                .texture("side", side);
    }

    public static InheritingModelGen cube(String north, String east, String south, String west, String up, String down) {
        return new InheritingModelGen("block/cube")
                .texture("north", north)
                .texture("east", east)
                .texture("south", south)
                .texture("west", west)
                .texture("up", up)
                .texture("down", down);
    }

    public static InheritingModelGen cubeMirrored(String north, String east, String south, String west, String up, String down) {
        return new InheritingModelGen("block/cube_mirrored")
                .texture("north", north)
                .texture("east", east)
                .texture("south", south)
                .texture("west", west)
                .texture("up", up)
                .texture("down", down);
    }

    public static InheritingModelGen cubeFrontSided(String front, String side, String top, String bottom) {
        return new InheritingModelGen("block/cube")
                   .texture("north", front)
                   .texture("east", side)
                   .texture("south", side)
                   .texture("west", side)
                   .texture("up", top)
                   .texture("down", bottom);
    }

    public static InheritingModelGen cubeFrontBackSided(String front, String back, String side, String top, String bottom) {
        return new InheritingModelGen("block/cube")
                   .texture("north", front)
                   .texture("east", side)
                   .texture("south", back)
                   .texture("west", side)
                   .texture("up", top)
                   .texture("down", bottom);
    }

    public static InheritingModelGen grassBlock(String top, String side, String bottom, String overlay) {
        return new InheritingModelGen("midnight:block/grass_block")
                   .texture("top", top)
                   .texture("bottom", bottom)
                   .texture("side", side)
                   .texture("overlay", overlay);
    }

    public static InheritingModelGen generated(String... layers) {
        InheritingModelGen gen = new InheritingModelGen("item/generated");
        for(int i = 0, l = layers.length; i < l; i++) {
            gen.texture("layer" + i, layers[i]);
        }
        return gen;
    }

    public static InheritingModelGen cross(String texture) {
        return new InheritingModelGen("block/cross")
                   .texture("cross", texture);
    }

    public static InheritingModelGen tintedCross(String texture) {
        return new InheritingModelGen("block/tinted_cross")
                   .texture("cross", texture);
    }
}
