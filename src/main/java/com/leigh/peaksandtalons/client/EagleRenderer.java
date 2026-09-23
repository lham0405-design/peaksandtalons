package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Stable alpha renderer. This keeps EagleEntity client creation safe while
 * necklace/component assets and gameplay are validated. The dedicated modeled
 * eagle renderer is the next visual milestone and is intentionally isolated
 * from the playable asset build so it cannot block testing.
 */
public class EagleRenderer extends EntityRenderer<EagleEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        PeaksAndTalons.MOD_ID, "textures/entity/eagle.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EagleEntity entity) {
        return TEXTURE;
    }
}
