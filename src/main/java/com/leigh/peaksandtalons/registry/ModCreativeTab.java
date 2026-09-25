package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PeaksAndTalons.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.peaksandtalons")).icon(() -> new ItemStack(ModItems.EYE_OF_THE_EAGLE.get()))
        .displayItems((parameters, output) -> {
            output.accept(ModItems.EAGLE_SPAWN_EGG.get()); output.accept(ModItems.ORDUK_SPAWN_EGG.get()); output.accept(ModItems.EAGLE_FEATHER.get()); output.accept(ModItems.EAGLE_TALON.get());
            output.accept(ModItems.CRUSHED_TALON.get()); output.accept(ModItems.TALON_DUST.get()); output.accept(ModItems.ANCIENT_TALON.get());
            output.accept(ModItems.TALON_BLADE.get()); output.accept(ModItems.TALONSPIRE.get()); output.accept(ModItems.TALONTEER.get());
            output.accept(ModItems.EYE_OF_THE_EAGLE.get()); output.accept(ModItems.STORM_CRYSTAL.get()); output.accept(ModItems.EYE_OF_THE_SUMMIT.get());
            output.accept(ModItems.GOLDEN_EAGLE_HARNESS.get()); output.accept(ModItems.HEART_OF_ORDUK.get());
            output.accept(ModItems.IRON_CHAIN.get()); output.accept(ModItems.GOLD_CHAIN.get()); output.accept(ModItems.NETHERITE_CHAIN.get());
            output.accept(ModItems.REDSTONE_SOCKET.get()); output.accept(ModItems.DRAGONS_EGG_SOCKET.get());
            ModItems.NECKLACES.values().forEach(item -> output.accept(item.get()));
        }).build());
    private ModCreativeTab() {}
}
