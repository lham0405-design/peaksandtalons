package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(PeaksAndTalons.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<EagleEntity>> EAGLE = ENTITIES.register("eagle", () ->
        EntityType.Builder.of(EagleEntity::new, MobCategory.CREATURE)
            .sized(1.3f, 1.1f)
            .clientTrackingRange(10)
            .build("eagle"));
    private ModEntities() {}
}
