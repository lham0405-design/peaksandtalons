package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = PeaksAndTalons.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientEvents {
    @SubscribeEvent public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EAGLE.get(), EagleRenderer::new);
        event.registerEntityRenderer(ModEntities.ORDUK.get(), OrdukRenderer::new);
    }
    @SubscribeEvent public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EagleModel.LAYER_LOCATION, EagleModel::createBodyLayer);
        event.registerLayerDefinition(OrdukModel.LAYER_LOCATION, OrdukModel::createBodyLayer);
    }
    private ClientEvents() {}
}
