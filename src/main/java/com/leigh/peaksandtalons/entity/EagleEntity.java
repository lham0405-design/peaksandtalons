package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModEntities;
import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EagleEntity extends TamableAnimal {
 private boolean saddled,vengeanceSpawned,trustReady;
 private int attackAnimationTicks,tameProgress,tamingRideTicks,tamingGoalTicks;
 private double tamingStartY;
 public EagleEntity(EntityType<? extends EagleEntity> type,Level level){super(type,level);}
 public static AttributeSupplier.Builder attributes(){return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,30).add(Attributes.MOVEMENT_SPEED,.38).add(Attributes.FLYING_SPEED,.82).add(Attributes.ATTACK_DAMAGE,7).add(Attributes.FOLLOW_RANGE,48);}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(1,new SitWhenOrderedToGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1.65,true));goalSelector.addGoal(3,new FollowOwnerGoal(this,1.35,6,2));goalSelector.addGoal(4,new RandomStrollGoal(this,1.15));goalSelector.addGoal(5,new LookAtPlayerGoal(this,Player.class,12));goalSelector.addGoal(6,new RandomLookAroundGoal(this));targetSelector.addGoal(1,new HurtByTargetGoal(this).setAlertOthers(EagleEntity.class));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,10,true,false,p->vengeanceSpawned&&!isTame()));}
 @Override public boolean doHurtTarget(Entity target){attackAnimationTicks=10;boolean hit=super.doHurtTarget(target);if(hit&&target instanceof LivingEntity l){Vec3 away=l.position().subtract(position()).normalize().scale(.45);l.push(away.x,-.08,away.z);}return hit;}
 public int getAttackAnimationTicks(){return attackAnimationTicks;}
 @Override public boolean isFood(ItemStack stack){return stack.is(Items.SALMON);}
 @Override public InteractionResult mobInteract(Player player,InteractionHand hand){
  ItemStack stack=player.getItemInHand(hand);
  if(!isTame()&&stack.is(Items.SALMON)){if(!player.getAbilities().instabuild)stack.shrink(1);trustReady=true;level().broadcastEntityEvent(this,(byte)7);return InteractionResult.sidedSuccess(level().isClientSide);}
  if(!isTame()&&trustReady&&!player.isShiftKeyDown()&&stack.isEmpty()){if(!level().isClientSide){tamingRideTicks=0;tamingGoalTicks=100+random.nextInt(81);tamingStartY=getY();player.startRiding(this);}return InteractionResult.sidedSuccess(level().isClientSide);}
  if(isTame()&&isOwnedBy(player)&&!saddled&&stack.is(Items.SADDLE)){saddled=true;setOrderedToSit(false);if(!player.getAbilities().instabuild)stack.shrink(1);return InteractionResult.sidedSuccess(level().isClientSide);}
  if(isTame()&&isOwnedBy(player)&&saddled&&!player.isShiftKeyDown()){setOrderedToSit(false);if(!level().isClientSide)player.startRiding(this);return InteractionResult.sidedSuccess(level().isClientSide);}
  return super.mobInteract(player,hand);
 }
 @Override public void tick(){
  super.tick();if(attackAnimationTicks>0)attackAnimationTicks--;
  if(!isTame()&&trustReady&&isVehicle()&&getFirstPassenger() instanceof Player rider){
   tamingRideTicks++;setNoGravity(true);fallDistance=0;
   double targetRise=Math.min(18+(tameProgress*2),26);double rise=getY()-tamingStartY;
   double spiral=(tickCount%80)/80.0*Math.PI*2;Vec3 v=getDeltaMovement().scale(.78).add(Math.cos(spiral)*.035,rise<targetRise?.075:.015,Math.sin(spiral)*.035);setDeltaMovement(v);
   if(tamingRideTicks>=tamingGoalTicks){tameProgress++;trustReady=false;ejectPassengers();setNoGravity(false);if(tameProgress>=2+random.nextInt(3)){tame(rider);setOrderedToSit(false);level().broadcastEntityEvent(this,(byte)7);if(!rider.addItem(new ItemStack(ModItems.EYE_OF_THE_EAGLE.get())))spawnAtLocation(ModItems.EYE_OF_THE_EAGLE.get());}else level().broadcastEntityEvent(this,(byte)6);}
  }else{setNoGravity(saddled&&isVehicle());if(isVehicle())fallDistance=0;}
 }
 @Override public void travel(Vec3 input){LivingEntity rider=getControllingPassenger();if(isTame()&&saddled&&rider instanceof Player){setYRot(rider.getYRot());yRotO=getYRot();setXRot(rider.getXRot()*.45F);float forward=rider.zza,strafe=rider.xxa*.55F;double vertical=-rider.getXRot()/72D;if(Math.abs(vertical)<.06)vertical=0;double speed=getAttributeValue(Attributes.FLYING_SPEED);Vec3 look=rider.getLookAngle();Vec3 desired=new Vec3(look.x*forward+strafe*Math.cos(Math.toRadians(rider.getYRot())),vertical,look.z*forward+strafe*Math.sin(Math.toRadians(rider.getYRot())));if(desired.lengthSqr()>.001)desired=desired.normalize().scale(speed);setDeltaMovement(getDeltaMovement().scale(.72).add(desired.scale(.28)));move(MoverType.SELF,getDeltaMovement());return;}super.travel(input);}
 @Nullable @Override public LivingEntity getControllingPassenger(){return getFirstPassenger() instanceof LivingEntity l?l:null;}
 @Override protected boolean canAddPassenger(Entity p){return getPassengers().isEmpty()&&p instanceof Player;}
 @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);tag.putBoolean("Saddled",saddled);tag.putBoolean("VengeanceSpawned",vengeanceSpawned);tag.putBoolean("TrustReady",trustReady);tag.putInt("TameProgress",tameProgress);}
 @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);saddled=tag.getBoolean("Saddled");vengeanceSpawned=tag.getBoolean("VengeanceSpawned");trustReady=tag.getBoolean("TrustReady");tameProgress=tag.getInt("TameProgress");}
 @Override public void die(DamageSource source){boolean retaliate=!isTame()&&!vengeanceSpawned&&!level().isClientSide;LivingEntity killer=source.getEntity() instanceof LivingEntity l?l:null;super.die(source);if(retaliate&&level() instanceof ServerLevel server){vengeanceSpawned=true;for(int i=0;i<3;i++){EagleEntity eagle=ModEntities.EAGLE.get().create(server);if(eagle!=null){eagle.vengeanceSpawned=true;eagle.moveTo(getX()+random.nextInt(7)-3,getY()+3+random.nextInt(3),getZ()+random.nextInt(7)-3,random.nextFloat()*360,0);server.addFreshEntity(eagle);if(killer!=null)eagle.setTarget(killer);}}}}
 @Nullable @Override public EagleEntity getBreedOffspring(ServerLevel level,AgeableMob other){return ModEntities.EAGLE.get().create(level);}
}
