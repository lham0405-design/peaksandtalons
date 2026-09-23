package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModEntities;
import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EagleEntity extends TamableAnimal {
    private boolean saddled;
    private boolean vengeanceSpawned;
    private int attackAnimationTicks;

    public EagleEntity(EntityType<? extends EagleEntity> type, Level level) { super(type, level); }

    public static AttributeSupplier.Builder attributes() {
        return LivingEntity.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 30)
            .add(Attributes.MOVEMENT_SPEED, 0.38)
            .add(Attributes.FLYING_SPEED, 0.82)
            .add(Attributes.ATTACK_DAMAGE, 7)
            .add(Attributes.FOLLOW_RANGE, 48);
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.65, true));
        goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.35, 6, 2));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 1.15));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(EagleEntity.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
            p -> vengeanceSpawned && !isTame()));
    }

    @Override public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        attackAnimationTicks = 10;
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            // Talon rake: a small knockback/downward shove makes the strike feel different from a normal bite.
            Vec3 away = living.position().subtract(position()).normalize().scale(0.45);
            living.push(away.x, -0.08, away.z);
        }
        return hit;
    }

    public int getAttackAnimationTicks() { return attackAnimationTicks; }

    @Override public boolean isFood(ItemStack stack) { return isFish(stack); }
    private boolean isFish(ItemStack stack) { return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH); }

    @Override public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isTame() && isFish(stack)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!level().isClientSide && random.nextInt(3) == 0) {
                tame(player); setOrderedToSit(false); level().broadcastEntityEvent(this, (byte)7);
                if (!player.addItem(new ItemStack(ModItems.EYE_OF_THE_EAGLE.get()))) spawnAtLocation(ModItems.EYE_OF_THE_EAGLE.get());
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && isOwnedBy(player) && !saddled && stack.is(Items.SADDLE)) {
            saddled=true; setOrderedToSit(false); if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && isOwnedBy(player) && saddled && !player.isShiftKeyDown()) {
            setOrderedToSit(false); if (!level().isClientSide) player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override public void tick() {
        super.tick();
        if (attackAnimationTicks > 0) attackAnimationTicks--;
        setNoGravity(saddled && isVehicle());
        if (isVehicle()) fallDistance=0;
    }

    @Override public void travel(Vec3 input) {
        LivingEntity rider=getControllingPassenger();
        if (saddled && rider instanceof Player) {
            setYRot(rider.getYRot()); yRotO=getYRot(); setXRot(rider.getXRot()*.45F);
            float forward=rider.zza; float strafe=rider.xxa*.55F;
            double vertical=-rider.getXRot()/72.0D; if(Math.abs(vertical)<.06D) vertical=0;
            double speed=getAttributeValue(Attributes.FLYING_SPEED);
            Vec3 look=rider.getLookAngle();
            Vec3 desired=new Vec3(look.x*forward + strafe*MthHelper.cosYaw(rider.getYRot()), vertical, look.z*forward + strafe*MthHelper.sinYaw(rider.getYRot()));
            if(desired.lengthSqr()>.001) desired=desired.normalize().scale(speed);
            setDeltaMovement(getDeltaMovement().scale(.72).add(desired.scale(.28)));
            move(net.minecraft.world.entity.MoverType.SELF,getDeltaMovement());
            return;
        }
        super.travel(input);
    }

    @Nullable @Override public LivingEntity getControllingPassenger(){return getFirstPassenger() instanceof LivingEntity l?l:null;}
    @Override protected boolean canAddPassenger(net.minecraft.world.entity.Entity p){return getPassengers().isEmpty()&&p instanceof Player;}

    @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);tag.putBoolean("Saddled",saddled);tag.putBoolean("VengeanceSpawned",vengeanceSpawned);}
    @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);saddled=tag.getBoolean("Saddled");vengeanceSpawned=tag.getBoolean("VengeanceSpawned");}

    @Override public void die(DamageSource source) {
        boolean retaliate=!isTame()&&!vengeanceSpawned&&!level().isClientSide;
        LivingEntity killer=source.getEntity() instanceof LivingEntity l?l:null;
        super.die(source);
        if(retaliate&&level() instanceof ServerLevel server){
            vengeanceSpawned=true;
            for(int i=0;i<3;i++){
                EagleEntity eagle=ModEntities.EAGLE.get().create(server);
                if(eagle!=null){
                    eagle.vengeanceSpawned=true;
                    eagle.moveTo(getX()+random.nextInt(7)-3,getY()+3+random.nextInt(3),getZ()+random.nextInt(7)-3,random.nextFloat()*360,0);
                    server.addFreshEntity(eagle);
                    if(killer!=null) eagle.setTarget(killer);
                }
            }
        }
    }

    @Nullable @Override public EagleEntity getBreedOffspring(ServerLevel level,net.minecraft.world.entity.AgeableMob other){return ModEntities.EAGLE.get().create(level);}

    private static final class MthHelper {
        static double cosYaw(float yaw){return Math.cos(Math.toRadians(yaw));}
        static double sinYaw(float yaw){return Math.sin(Math.toRadians(yaw));}
    }
}
