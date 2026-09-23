package com.leigh.peaksandtalons.client;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.entity.EagleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EagleModel extends HierarchicalModel<EagleEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID, "eagle"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public EagleModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = body.getChild("head");
        this.leftWing = body.getChild("left_wing");
        this.rightWing = body.getChild("right_wing");
        this.tail = body.getChild("tail");
        this.leftLeg = body.getChild("left_leg");
        this.rightLeg = body.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -6.0F, -7.0F, 10.0F, 11.0F, 15.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 27).addBox(-4.0F, -5.0F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -4.0F, -7.0F));
        head.addOrReplaceChild("beak",
            CubeListBuilder.create().texOffs(32, 28).addBox(-2.0F, -1.5F, -5.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, -5.0F));

        body.addOrReplaceChild("left_wing",
            CubeListBuilder.create().texOffs(0, 44).addBox(0.0F, -1.0F, -2.0F, 16.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)),
            PartPose.offset(4.5F, -3.0F, -2.0F));
        body.addOrReplaceChild("right_wing",
            CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-16.0F, -1.0F, -2.0F, 16.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(-4.5F, -3.0F, -2.0F));
        body.addOrReplaceChild("tail",
            CubeListBuilder.create().texOffs(42, 0).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 1.0F, 7.0F));
        body.addOrReplaceChild("left_leg",
            CubeListBuilder.create().texOffs(52, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(2.5F, 4.0F, 1.0F));
        body.addOrReplaceChild("right_leg",
            CubeListBuilder.create().texOffs(52, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-2.5F, 4.0F, 1.0F));

        return LayerDefinition.create(mesh, 96, 64);
    }

    @Override
    public void setupAnim(EagleEntity eagle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot = headPitch * ((float)Math.PI / 180F);

        boolean airborne = !eagle.onGround() || eagle.getDeltaMovement().y > 0.08D;
        if (airborne) {
            float flap = Mth.sin(ageInTicks * 0.65F) * 0.75F;
            leftWing.zRot = -0.25F - flap;
            rightWing.zRot = 0.25F + flap;
            leftWing.xRot = -0.15F;
            rightWing.xRot = -0.15F;
            tail.xRot = 0.20F;
            leftLeg.xRot = 0.55F;
            rightLeg.xRot = 0.55F;
        } else {
            leftWing.zRot = -0.10F;
            rightWing.zRot = 0.10F;
            float walk = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
            leftLeg.xRot = walk;
            rightLeg.xRot = -walk;
            body.y = Mth.sin(ageInTicks * 0.10F) * 0.15F;
        }
    }

    @Override
    public ModelPart root() { return root; }
}
