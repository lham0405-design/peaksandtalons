package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EagleRenderer extends MobRenderer<EagleEntity,EagleModel>{
 private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID,"textures/entity/eagle.png");
 public EagleRenderer(EntityRendererProvider.Context c){super(c,new EagleModel(c.bakeLayer(EagleModel.LAYER_LOCATION)),1.15F);}
 @Override protected void scale(EagleEntity e,PoseStack p,float pt){p.scale(1.78F,1.78F,1.78F);}
 @Override public ResourceLocation getTextureLocation(EagleEntity e){return TEXTURE;}
}
