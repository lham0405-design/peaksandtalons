package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Playable-alpha eagle renderer. It reuses the vanilla parrot renderer/model
 * so the custom EagleEntity has a stable visible body on NeoForge 1.21.1.
 * A dedicated eagle model/texture can replace this after gameplay validation.
 */
public class EagleRenderer extends ParrotRenderer {
    private static final ResourceLocation EAGLE_TEXTURE =
        ResourceLocation.withDefaultNamespace("textures/entity/parrot/parrot_grey.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Parrot parrot) {
        return EAGLE_TEXTURE;
    }
}
