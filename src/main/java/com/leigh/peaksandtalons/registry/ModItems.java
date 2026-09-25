package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
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
    public static final DeferredItem<Item> EAGLE_FEATHER = ITEMS.registerSimpleItem("eagle_feather", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> EAGLE_TALON = ITEMS.registerSimpleItem("eagle_talon", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> CRUSHED_TALON = ITEMS.registerSimpleItem("crushed_talon", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> TALON_DUST = ITEMS.registerSimpleItem("talon_dust", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> STORM_CRYSTAL = ITEMS.registerSimpleItem("storm_crystal", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> ANCIENT_TALON = ITEMS.registerSimpleItem("ancient_talon", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> TALON_BLADE = ITEMS.registerSimpleItem("talon_blade", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<PoisonTalonWeaponItem> TALONSPIRE = ITEMS.registerItem("talonspire", p -> new PoisonTalonWeaponItem(Tiers.IRON, p.attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<PoisonTalonWeaponItem> TALONTEER = ITEMS.registerItem("talonteer", p -> new PoisonTalonWeaponItem(Tiers.IRON, p.attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.8F)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EYE_OF_THE_SUMMIT = ITEMS.registerSimpleItem("eye_of_the_summit", new Item.Properties().rarity(Rarity.EPIC));
    public static final DeferredItem<Item> GOLDEN_EAGLE_HARNESS = ITEMS.registerSimpleItem("golden_eagle_harness", new Item.Properties().rarity(Rarity.EPIC).stacksTo(1));
    public static final DeferredItem<TrollHeartItem> TROLL_HEART = ITEMS.registerItem("troll_heart", p -> new TrollHeartItem(p.rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> DRAGONS_EGG_SOCKET = ITEMS.registerSimpleItem("dragons_egg_socket", new Item.Properties().rarity(Rarity.EPIC));
    public static final DeferredItem<EagleSpawnEggItem> EAGLE_SPAWN_EGG = ITEMS.registerItem("eagle_spawn_egg", EagleSpawnEggItem::new);
    public static final DeferredItem<OrdukSpawnEggItem> ORDUK_SPAWN_EGG = ITEMS.registerItem("orduk_spawn_egg", OrdukSpawnEggItem::new);
    public static final Map<String, DeferredItem<NecklaceItem>> NECKLACES = new LinkedHashMap<>();
    static { for (ChainMaterial chain : ChainMaterial.values()) for (SocketType socket : SocketType.values()) { String id=chain.name().toLowerCase()+"_"+socket.name().toLowerCase()+"_necklace"; NECKLACES.put(id, ITEMS.registerItem(id,p->new NecklaceItem(chain,socket,p))); } }
    private ModItems() {}
}
