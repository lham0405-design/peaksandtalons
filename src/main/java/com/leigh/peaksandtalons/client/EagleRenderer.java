package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.entity.EagleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Full alpha Eagle renderer. Uses a guaranteed vanilla-bundled bird texture so
 * the Eagle can never fall back to the missing-texture checkerboard. */
public class EagleRenderer extends MobRenderer<EagleEntity, EagleModel> {
    private static final ResourceLocation TEXTURE =
        ResourceLocation.withDefaultNamespace("textures/entity/parrot/parrot_grey.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel(context.bakeLayer(EagleModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    protected void scale(EagleEntity eagle, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.35F, 1.35F, 1.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(EagleEntity entity) {
        return TEXTURE;
    }
}
