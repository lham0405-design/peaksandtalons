package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PeaksAndTalons.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<EagleEntity>> EAGLE = ENTITIES.register("eagle", () ->
        EntityType.Builder.of(EagleEntity::new, MobCategory.CREATURE)
            // The eagle is intentionally boss-scale. Keep the interaction/combat volume
            // close to the rendered bird so airborne hits register on the bird itself.
            .sized(2.65f, 2.15f)
            .clientTrackingRange(12)
            .build("eagle"));

    private ModEntities() {}
}
