package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EagleModel extends HierarchicalModel<EagleEntity>{
 public static final ModelLayerLocation LAYER_LOCATION=new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID,"eagle_v5"),"main");
 private final ModelPart root,body,neck,head,leftWing,rightWing,leftForeWing,rightForeWing,tail,leftLeg,rightLeg,saddle;
 public EagleModel(ModelPart root){this.root=root;body=root.getChild("body");neck=body.getChild("neck");head=neck.getChild("head");leftWing=body.getChild("left_wing");rightWing=body.getChild("right_wing");leftForeWing=leftWing.getChild("forewing");rightForeWing=rightWing.getChild("forewing");tail=body.getChild("tail");leftLeg=body.getChild("left_leg");rightLeg=body.getChild("right_leg");saddle=body.getChild("saddle");}
 public static LayerDefinition createBodyLayer(){MeshDefinition mesh=new MeshDefinition();PartDefinition r=mesh.getRoot();
  PartDefinition b=r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,0).addBox(-5F,-5.5F,-6.5F,10F,11F,14F).texOffs(0,25).addBox(-4F,-6.5F,-4.5F,8F,3F,10F),PartPose.offset(0,13,1));
  b.addOrReplaceChild("saddle",CubeListBuilder.create().texOffs(0,0).addBox(-5.4F,-1.2F,-4.5F,10.8F,2F,9F).texOffs(0,12).addBox(-5.8F,-2.8F,-1.2F,1.4F,7F,2.4F).texOffs(8,12).addBox(4.4F,-2.8F,-1.2F,1.4F,7F,2.4F).texOffs(16,12).addBox(-5F,-3F,2.8F,10F,3F,2F),PartPose.offset(0,-5.2F,.5F));
  PartDefinition n=b.addOrReplaceChild("neck",CubeListBuilder.create().texOffs(34,0).addBox(-3.7F,-4F,-4F,7.4F,7F,7F),PartPose.offset(0,-4,-6.5F));
  PartDefinition h=n.addOrReplaceChild("head",CubeListBuilder.create().texOffs(34,15).addBox(-3.4F,-4F,-4.8F,6.8F,6.3F,6.5F),PartPose.offset(0,-1.5F,-2.8F));
  h.addOrReplaceChild("brow",CubeListBuilder.create().texOffs(58,0).addBox(-3.6F,-2.5F,-5.4F,7.2F,1.3F,2.2F),PartPose.ZERO);
  h.addOrReplaceChild("left_eye",CubeListBuilder.create().texOffs(88,0).addBox(-.15F,-.55F,-.15F,.3F,1.1F,1.1F),PartPose.offset(3.38F,-1.35F,-4.25F));
  h.addOrReplaceChild("right_eye",CubeListBuilder.create().texOffs(88,0).addBox(-.15F,-.55F,-.15F,.3F,1.1F,1.1F),PartPose.offset(-3.38F,-1.35F,-4.25F));
  PartDefinition beak=h.addOrReplaceChild("beak",CubeListBuilder.create().texOffs(58,5).addBox(-2.2F,-1.5F,-5.8F,4.4F,3F,5.8F),PartPose.offset(0,0,-4.2F));beak.addOrReplaceChild("hook",CubeListBuilder.create().texOffs(60,14).addBox(-1.5F,0,-3F,3F,2.8F,3.5F),PartPose.offset(0,.5F,-5F));
  PartDefinition lw=b.addOrReplaceChild("left_wing",CubeListBuilder.create().texOffs(0,39).addBox(0,-2F,-3.5F,11F,3F,10F),PartPose.offset(4.5F,-2,-1));PartDefinition lfw=lw.addOrReplaceChild("forewing",CubeListBuilder.create().texOffs(0,52).addBox(0,-1.3F,-3F,12F,2.6F,9F),PartPose.offset(10F,0,0));PartDefinition rw=b.addOrReplaceChild("right_wing",CubeListBuilder.create().texOffs(0,39).mirror().addBox(-11F,-2F,-3.5F,11F,3F,10F).mirror(false),PartPose.offset(-4.5F,-2,-1));PartDefinition rfw=rw.addOrReplaceChild("forewing",CubeListBuilder.create().texOffs(0,52).mirror().addBox(-12F,-1.3F,-3F,12F,2.6F,9F).mirror(false),PartPose.offset(-10F,0,0));
  for(int i=0;i<5;i++){lfw.addOrReplaceChild("primary"+i,CubeListBuilder.create().texOffs(40,30+i*4).addBox(0,-.7F,-1F,9F-i,1.4F,2F),PartPose.offset(8F-i*.5F,0,4F-i*2F));rfw.addOrReplaceChild("primary"+i,CubeListBuilder.create().texOffs(40,30+i*4).mirror().addBox(-(9F-i),-.7F,-1F,9F-i,1.4F,2F).mirror(false),PartPose.offset(-8F+i*.5F,0,4F-i*2F));}
  PartDefinition t=b.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(34,52).addBox(-4.5F,-1F,0,9F,2F,9F),PartPose.offset(0,1,7));PartDefinition ll=b.addOrReplaceChild("left_leg",CubeListBuilder.create().texOffs(72,0).addBox(-1.3F,0,-1.3F,2.6F,6.5F,2.6F),PartPose.offset(2.6F,4.5F,0));PartDefinition rl=b.addOrReplaceChild("right_leg",CubeListBuilder.create().texOffs(72,0).addBox(-1.3F,0,-1.3F,2.6F,6.5F,2.6F),PartPose.offset(-2.6F,4.5F,0));addFoot(ll);addFoot(rl);return LayerDefinition.create(mesh,96,64);}
 private static void addFoot(PartDefinition leg){PartDefinition f=leg.addOrReplaceChild("foot",CubeListBuilder.create().texOffs(72,10).addBox(-2.3F,-.8F,-3.8F,4.6F,1.6F,5.8F),PartPose.offset(0,6.5F,0));for(int i=-1;i<=1;i++)f.addOrReplaceChild("talon"+(i+1),CubeListBuilder.create().texOffs(84,10).addBox(-.45F,-.45F,-4F,.9F,.9F,4F),PartPose.offset(i*1.45F,.1F,-2.8F));}
 @Override public void setupAnim(EagleEntity e,float limb,float amount,float age,float yaw,float pitch){root.getAllParts().forEach(ModelPart::resetPose);saddle.visible=e.isSaddled();head.yRot=yaw*Mth.DEG_TO_RAD;head.xRot=pitch*Mth.DEG_TO_RAD;boolean air=!e.onGround()||Math.abs(e.getDeltaMovement().y)>.02;float speed=(float)e.getDeltaMovement().horizontalDistance();if(air){float rate=speed>.45F?.42F:.72F;float amp=speed>.55F?.42F:1.02F;float flap=Mth.sin(age*rate)*amp;leftWing.zRot=-.12F-flap;rightWing.zRot=.12F+flap;leftForeWing.zRot=-.08F-flap*.5F;rightForeWing.zRot=.08F+flap*.5F;leftWing.yRot=rightWing.yRot=0;tail.xRot=.18F;leftLeg.xRot=rightLeg.xRot=.72F;}else{leftWing.yRot=-1.28F;rightWing.yRot=1.28F;leftWing.zRot=-.12F;rightWing.zRot=.12F;leftForeWing.zRot=rightForeWing.zRot=0;float walk=Mth.cos(limb*.6662F)*.65F*amount;leftLeg.xRot=walk;rightLeg.xRot=-walk;}if(e.getAttackAnimationTicks()>0){float s=e.getAttackAnimationTicks()/12F,k=Mth.sin(s*Mth.PI);head.xRot-=.8F*k;neck.xRot=-.4F*k;leftLeg.xRot=-1.25F*k;rightLeg.xRot=-1.25F*k;leftWing.zRot-=.3F*k;rightWing.zRot+=.3F*k;}}
 @Override public ModelPart root(){return root;}
}
