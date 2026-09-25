package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.block.OrdukOfferingPodiumBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
 public static final DeferredRegister.Blocks BLOCKS=DeferredRegister.createBlocks(PeaksAndTalons.MOD_ID);
 public static final DeferredBlock<Block> ORDUK_OFFERING_PODIUM=BLOCKS.register("orduk_offering_podium",()->new OrdukOfferingPodiumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(-1.0F,3600000.0F).noLootTable()));
 private ModBlocks(){}
}
