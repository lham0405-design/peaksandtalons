package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PeaksAndTalons.MOD_ID);
    public static final DeferredItem<Item> IRON_CHAIN = ITEMS.registerSimpleItem("iron_chain");
    public static final DeferredItem<Item> GOLD_CHAIN = ITEMS.registerSimpleItem("gold_chain");
    public static final DeferredItem<Item> NETHERITE_CHAIN = ITEMS.registerSimpleItem("netherite_chain", new Item.Properties().fireResistant());
    public static final DeferredItem<Item> REDSTONE_SOCKET = ITEMS.registerSimpleItem("redstone_socket");
    public static final DeferredItem<Item> EYE_OF_THE_EAGLE = ITEMS.registerSimpleItem("eye_of_the_eagle", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> DRAGONS_EGG_SOCKET = ITEMS.registerSimpleItem("dragons_egg_socket", new Item.Properties().rarity(Rarity.EPIC));
    public static final DeferredItem<SpawnEggItem> EAGLE_SPAWN_EGG = ITEMS.registerItem("eagle_spawn_egg",
        properties -> new SpawnEggItem(properties.spawnEgg(ModEntities.EAGLE.get())));
    public static final Map<String, DeferredItem<NecklaceItem>> NECKLACES = new LinkedHashMap<>();

    static {
        for (ChainMaterial chain : ChainMaterial.values()) {
            for (SocketType socket : SocketType.values()) {
                String id = chain.name().toLowerCase() + "_" + socket.name().toLowerCase() + "_necklace";
                NECKLACES.put(id, ITEMS.registerItem(id, p -> new NecklaceItem(chain, socket, p)));
            }
        }
    }

    private ModItems() {}
}
