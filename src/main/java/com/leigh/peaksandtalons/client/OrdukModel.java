package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.OrdukEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Squat, broad troll silhouette with scavenged goblin plate, tusks and oversized striking arms. */
public class OrdukModel extends HierarchicalModel<OrdukEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID,"orduk"),"main");
    private final ModelPart root, body, head, leftArm, rightArm, leftLeg, rightLeg;
    public OrdukModel(ModelPart root){this.root=root;body=root.getChild("body");head=body.getChild("head");leftArm=body.getChild("left_arm");rightArm=body.getChild("right_arm");leftLeg=root.getChild("left_leg");rightLeg=root.getChild("right_leg");}
    public static LayerDefinition createBodyLayer(){
        MeshDefinition mesh=new MeshDefinition(); PartDefinition r=mesh.getRoot();
        PartDefinition b=r.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,0).addBox(-9,-10,-5,18,15,10).texOffs(0,25).addBox(-10,-9,-6,20,6,12),PartPose.offset(0,16,0));
        b.addOrReplaceChild("belly_armor",CubeListBuilder.create().texOffs(44,25).addBox(-7,-4,-6.2F,14,9,2),PartPose.ZERO);
        PartDefinition h=b.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,44).addBox(-6,-7,-6,12,9,10).texOffs(44,38).addBox(-6.5F,-7.5F,-5.5F,13,3,10),PartPose.offset(0,-10,-1));
        h.addOrReplaceChild("nose",CubeListBuilder.create().texOffs(36,52).addBox(-2,-1,-5,4,4,6),PartPose.offset(0,-2,-5));
        h.addOrReplaceChild("left_tusk",CubeListBuilder.create().texOffs(58,52).addBox(-.6F,-2,-.6F,1.2F,3,1.2F),PartPose.offset(3.2F,1,-6));
        h.addOrReplaceChild("right_tusk",CubeListBuilder.create().texOffs(58,52).addBox(-.6F,-2,-.6F,1.2F,3,1.2F),PartPose.offset(-3.2F,1,-6));
        h.addOrReplaceChild("helmet_horn_l",CubeListBuilder.create().texOffs(60,0).addBox(-1,-5,-1,2,6,2),PartPose.offset(5,-7,0));
        h.addOrReplaceChild("helmet_horn_r",CubeListBuilder.create().texOffs(60,0).addBox(-1,-5,-1,2,6,2),PartPose.offset(-5,-7,0));
        b.addOrReplaceChild("left_arm",CubeListBuilder.create().texOffs(64,0).addBox(0,-3,-4,7,17,8).texOffs(64,25).addBox(-.5F,8,-4.5F,8,7,9),PartPose.offset(8,-7,0));
        b.addOrReplaceChild("right_arm",CubeListBuilder.create().texOffs(64,0).mirror().addBox(-7,-3,-4,7,17,8).mirror(false).texOffs(64,25).mirror().addBox(-7.5F,8,-4.5F,8,7,9).mirror(false),PartPose.offset(-8,-7,0));
        r.addOrReplaceChild("left_leg",CubeListBuilder.create().texOffs(0,0).addBox(-3,0,-3,6,8,7),PartPose.offset(4,16,0));
        r.addOrReplaceChild("right_leg",CubeListBuilder.create().texOffs(0,0).addBox(-3,0,-3,6,8,7),PartPose.offset(-4,16,0));
        return LayerDefinition.create(mesh,128,64);
    }
    @Override public void setupAnim(OrdukEntity e,float limb,float amount,float age,float yaw,float pitch){root.getAllParts().forEach(ModelPart::resetPose);head.yRot=yaw*Mth.DEG_TO_RAD;head.xRot=pitch*Mth.DEG_TO_RAD*.45F;float walk=Mth.cos(limb*.7F)*.75F*amount;leftLeg.xRot=walk;rightLeg.xRot=-walk;leftArm.xRot=-walk*.55F;rightArm.xRot=walk*.55F;body.xRot=.08F;}
    @Override public ModelPart root(){return root;}
}
