package com.leigh.peaksandtalons;

import com.leigh.peaksandtalons.registry.ModEntities;
import com.leigh.peaksandtalons.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PeaksAndTalons.MOD_ID)
public final class PeaksAndTalons {
    public static final String MOD_ID = "peaksandtalons";

    public PeaksAndTalons(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
    }
}
