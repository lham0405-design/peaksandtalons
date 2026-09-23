package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Minimal renderer used by the first playable alpha. A modeled renderer replaces
 * this after gameplay validation; registering a renderer now keeps client entity
 * creation safe while the eagle mechanics are tested.
 */
public class EagleRenderer extends EntityRenderer<EagleEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID, "textures/entity/eagle.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EagleEntity entity) {
        return TEXTURE;
    }
}
