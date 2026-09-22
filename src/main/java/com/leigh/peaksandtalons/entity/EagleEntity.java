package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModEntities;
import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EagleEntity extends TamableAnimal {
    private boolean saddled;
    private boolean vengeanceSpawned;

    public EagleEntity(EntityType<? extends EagleEntity> type, Level level) { super(type, level); }

    public static AttributeSupplier.Builder attributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 30)
            .add(Attributes.MOVEMENT_SPEED, 0.32)
            .add(Attributes.FLYING_SPEED, 0.55)
            .add(Attributes.ATTACK_DAMAGE, 6);
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.2, 6, 2));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    private boolean isFish(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH);
    }

    @Override public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isTame() && isFish(stack)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!level().isClientSide && random.nextInt(3) == 0) {
                tame(player);
                level().broadcastEntityEvent(this, (byte) 7);
                player.addItem(new ItemStack(ModItems.EYE_OF_THE_EAGLE.get()));
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && isOwnedBy(player) && !saddled && stack.is(Items.SADDLE)) {
            saddled = true;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && isOwnedBy(player) && saddled && !player.isShiftKeyDown()) {
            if (!level().isClientSide) player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override public void travel(Vec3 input) {
        LivingEntity rider = getControllingPassenger();
        if (saddled && rider instanceof Player) {
            setYRot(rider.getYRot());
            setXRot(rider.getXRot() * 0.5F);
            float forward = rider.zza;
            float strafe = rider.xxa * 0.5F;
            double vertical = -rider.getXRot() / 90.0 * 0.6;
            setSpeed((float) getAttributeValue(Attributes.FLYING_SPEED));
            super.travel(new Vec3(strafe, vertical, forward));
            return;
        }
        super.travel(input);
    }

    @Nullable @Override public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity living ? living : null;
    }

    @Override public void die(net.minecraft.world.damagesource.DamageSource source) {
        boolean retaliate = !isTame() && !vengeanceSpawned && !level().isClientSide;
        super.die(source);
        if (retaliate && level() instanceof ServerLevel server) {
            vengeanceSpawned = true;
            for (int i = 0; i < 3; i++) {
                EagleEntity eagle = ModEntities.EAGLE.get().create(server);
                if (eagle != null) {
                    eagle.vengeanceSpawned = true;
                    eagle.moveTo(getX() + random.nextInt(7) - 3, getY() + 2, getZ() + random.nextInt(7) - 3, random.nextFloat() * 360, 0);
                    server.addFreshEntity(eagle);
                }
            }
        }
    }

    @Nullable @Override public EagleEntity getBreedOffspring(ServerLevel level, net.minecraft.world.entity.AgeableMob other) {
        return ModEntities.EAGLE.get().create(level);
    }
}
