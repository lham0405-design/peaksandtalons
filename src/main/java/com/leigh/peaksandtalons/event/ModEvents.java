package com.leigh.peaksandtalons.event;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import com.leigh.peaksandtalons.entity.OrdukEntity;
import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = PeaksAndTalons.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent public static void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.EAGLE.get(), EagleEntity.attributes().build());
        event.put(ModEntities.ORDUK.get(), OrdukEntity.attributes().build());
    }
    @SubscribeEvent public static void spawns(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.EAGLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (type, level, reason, pos, random) -> pos.getY() >= 140 && level.getMaxLocalRawBrightness(pos) > 8,
            RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
