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

public class EagleModel extends HierarchicalModel<EagleEntity> {
 public static final ModelLayerLocation LAYER_LOCATION=new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID,"eagle_v2"),"main");
 private final ModelPart root,body,neck,head,leftWing,rightWing,leftForeWing,rightForeWing,tail,leftLeg,rightLeg;
 public EagleModel(ModelPart root){this.root=root;body=root.getChild("body");neck=body.getChild("neck");head=neck.getChild("head");leftWing=body.getChild("left_wing");rightWing=body.getChild("right_wing");leftForeWing=leftWing.getChild("forewing");rightForeWing=rightWing.getChild("forewing");tail=body.getChild("tail");leftLeg=body.getChild("left_leg");rightLeg=body.getChild("right_leg");}
 public static LayerDefinition createBodyLayer(){
  MeshDefinition mesh=new MeshDefinition();PartDefinition r=mesh.getRoot();
  PartDefinition b=r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,0).addBox(-4.5F,-5F,-6F,9F,10F,13F).texOffs(0,23).addBox(-3.5F,-6F,-4F,7F,3F,9F),PartPose.offset(0,14,1));
  PartDefinition n=b.addOrReplaceChild("neck",CubeListBuilder.create().texOffs(34,0).addBox(-3.5F,-4F,-4F,7F,7F,7F),PartPose.offset(0,-3.5F,-6F));
  PartDefinition h=n.addOrReplaceChild("head",CubeListBuilder.create().texOffs(34,15).addBox(-3.2F,-4F,-4.5F,6.4F,6F,6F),PartPose.offset(0,-1.5F,-2.5F));
  h.addOrReplaceChild("brow",CubeListBuilder.create().texOffs(58,0).addBox(-3.4F,-2.4F,-5F,6.8F,1.2F,2F),PartPose.ZERO);
  PartDefinition beak=h.addOrReplaceChild("beak",CubeListBuilder.create().texOffs(58,5).addBox(-2F,-1.3F,-5F,4F,2.6F,5F),PartPose.offset(0,0,-4F));
  beak.addOrReplaceChild("hook",CubeListBuilder.create().texOffs(60,13).addBox(-1.4F,0F,-2.5F,2.8F,2.4F,3F),PartPose.offset(0,.4F,-4.2F));
  PartDefinition lw=b.addOrReplaceChild("left_wing",CubeListBuilder.create().texOffs(0,38).addBox(0,-2F,-3F,10F,3F,9F),PartPose.offset(4,-2F,-1F));
  PartDefinition lfw=lw.addOrReplaceChild("forewing",CubeListBuilder.create().texOffs(0,51).addBox(0,-1.2F,-2.5F,11F,2.4F,8F),PartPose.offset(9F,0,0));
  for(int i=0;i<4;i++)lfw.addOrReplaceChild("primary"+i,CubeListBuilder.create().texOffs(40,30+i*4).addBox(0,-.6F,-1F,8F-i,1.2F,2F),PartPose.offset(7F-i*.4F,0,3F-i*2F));
  PartDefinition rw=b.addOrReplaceChild("right_wing",CubeListBuilder.create().texOffs(0,38).mirror().addBox(-10F,-2F,-3F,10F,3F,9F).mirror(false),PartPose.offset(-4,-2F,-1F));
  PartDefinition rfw=rw.addOrReplaceChild("forewing",CubeListBuilder.create().texOffs(0,51).mirror().addBox(-11F,-1.2F,-2.5F,11F,2.4F,8F).mirror(false),PartPose.offset(-9F,0,0));
  for(int i=0;i<4;i++)rfw.addOrReplaceChild("primary"+i,CubeListBuilder.create().texOffs(40,30+i*4).mirror().addBox(-(8F-i),-.6F,-1F,8F-i,1.2F,2F).mirror(false),PartPose.offset(-7F+i*.4F,0,3F-i*2F));
  PartDefinition t=b.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(34,52).addBox(-4F,-1F,0,8F,2F,8F),PartPose.offset(0,1F,6F));
  t.addOrReplaceChild("tail_l",CubeListBuilder.create().texOffs(64,22).addBox(0,-.7F,0,4F,1.4F,8F),PartPose.offset(1.5F,0,5F));t.addOrReplaceChild("tail_r",CubeListBuilder.create().texOffs(64,22).mirror().addBox(-4F,-.7F,0,4F,1.4F,8F).mirror(false),PartPose.offset(-1.5F,0,5F));
  PartDefinition ll=b.addOrReplaceChild("left_leg",CubeListBuilder.create().texOffs(72,0).addBox(-1.2F,0,-1.2F,2.4F,6F,2.4F),PartPose.offset(2.4F,4F,0));PartDefinition rl=b.addOrReplaceChild("right_leg",CubeListBuilder.create().texOffs(72,0).addBox(-1.2F,0,-1.2F,2.4F,6F,2.4F),PartPose.offset(-2.4F,4F,0));addFoot(ll);addFoot(rl);return LayerDefinition.create(mesh,96,64);
 }
 private static void addFoot(PartDefinition leg){PartDefinition f=leg.addOrReplaceChild("foot",CubeListBuilder.create().texOffs(72,10).addBox(-2F,-.7F,-3F,4F,1.4F,5F),PartPose.offset(0,6F,0));for(int i=-1;i<=1;i++)f.addOrReplaceChild("talon"+(i+1),CubeListBuilder.create().texOffs(84,10).addBox(-.35F,-.35F,-3F,.7F,.7F,3F),PartPose.offset(i*1.2F,0,-2F));}
 @Override public void setupAnim(EagleEntity e,float limb,float amount,float age,float yaw,float pitch){
  root.getAllParts().forEach(ModelPart::resetPose);head.yRot=yaw*Mth.DEG_TO_RAD;head.xRot=pitch*Mth.DEG_TO_RAD;
  boolean air=!e.onGround()||e.getDeltaMovement().y>.05;float horizontal=(float)e.getDeltaMovement().horizontalDistance();
  if(air){float flapRate=horizontal<.18F?.78F:.48F;float flap=Mth.sin(age*flapRate)*.72F;leftWing.zRot=-.18F-flap;rightWing.zRot=.18F+flap;leftForeWing.zRot=-.10F-flap*.42F;rightForeWing.zRot=.10F+flap*.42F;leftWing.xRot=rightWing.xRot=-.12F;tail.xRot=.18F;leftLeg.xRot=rightLeg.xRot=.62F;}
  else{leftWing.zRot=-1.18F;rightWing.zRot=1.18F;leftForeWing.zRot=-.32F;rightForeWing.zRot=.32F;float walk=Mth.cos(limb*.6662F)*.7F*amount;leftLeg.xRot=walk;rightLeg.xRot=-walk;body.y=Mth.sin(age*.1F)*.08F;}
  if(e.getAttackAnimationTicks()>0){float strike=e.getAttackAnimationTicks()/10F;head.xRot-=.65F*Mth.sin(strike*Mth.PI);neck.xRot=-.30F*Mth.sin(strike*Mth.PI);leftLeg.xRot=-1.05F;rightLeg.xRot=-1.05F;leftWing.zRot-=.22F;rightWing.zRot+=.22F;}
 }
 @Override public ModelPart root(){return root;}
}
