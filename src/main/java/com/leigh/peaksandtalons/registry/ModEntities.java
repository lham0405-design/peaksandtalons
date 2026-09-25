package com.leigh.peaksandtalons.registry;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import com.leigh.peaksandtalons.entity.OrdukEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PeaksAndTalons.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<EagleEntity>> EAGLE = ENTITIES.register("eagle", () -> EntityType.Builder.of(EagleEntity::new, MobCategory.CREATURE).sized(2.65f, 2.15f).clientTrackingRange(12).build("eagle"));
    public static final DeferredHolder<EntityType<?>, EntityType<OrdukEntity>> ORDUK = ENTITIES.register("orduk", () -> EntityType.Builder.of(OrdukEntity::new, MobCategory.MONSTER).sized(2.25f, 2.85f).clientTrackingRange(12).fireImmune().build("orduk"));
    private ModEntities() {}
}
