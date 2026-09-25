package com.leigh.peaksandtalons.client;
import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.OrdukEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
public class OrdukRenderer extends MobRenderer<OrdukEntity,OrdukModel>{
 private static final ResourceLocation TEX=ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID,"textures/entity/orduk.png");
 public OrdukRenderer(EntityRendererProvider.Context c){super(c,new OrdukModel(c.bakeLayer(OrdukModel.LAYER_LOCATION)),1.25F);}
 @Override public ResourceLocation getTextureLocation(OrdukEntity e){return TEX;}
}
