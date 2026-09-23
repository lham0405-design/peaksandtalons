package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.entity.EagleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Eagle V2 renderer. Custom geometry; temporary brown vanilla texture until the dedicated pixel skin is packaged. */
public class EagleRenderer extends MobRenderer<EagleEntity,EagleModel>{
 private static final ResourceLocation TEXTURE=ResourceLocation.withDefaultNamespace("textures/entity/horse/horse_brown.png");
 public EagleRenderer(EntityRendererProvider.Context c){super(c,new EagleModel(c.bakeLayer(EagleModel.LAYER_LOCATION)),.65F);}
 @Override protected void scale(EagleEntity e,PoseStack p,float pt){p.scale(1.05F,1.05F,1.05F);}
 @Override public ResourceLocation getTextureLocation(EagleEntity e){return TEXTURE;}
}
