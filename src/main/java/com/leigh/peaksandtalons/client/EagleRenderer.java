package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Visible alpha renderer. Uses Minecraft's bird geometry while the final custom
 * eagle/GeckoLib model is developed, so the entity is testable instead of invisible. */
public class EagleRenderer extends MobRenderer<EagleEntity, ParrotModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID, "textures/entity/eagle.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new ParrotModel(context.bakeLayer(ModelLayers.PARROT)), 0.45F);
    }

    @Override
    public ResourceLocation getTextureLocation(EagleEntity entity) {
        return TEXTURE;
    }
}
