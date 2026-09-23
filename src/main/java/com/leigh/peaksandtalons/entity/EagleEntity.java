package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModEntities;
import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EagleEntity extends TamableAnimal {
 private boolean saddled,vengeanceSpawned,trustReady;
 private int attackAnimationTicks,tameProgress,tamingRideTicks,tamingGoalTicks;
 private double tamingStartY;
 private ServerBossEvent encounterBar;
 private List<EagleEntity> encounterMembers=new ArrayList<>();

 public EagleEntity(EntityType<? extends EagleEntity> type,Level level){
  super(type,level);moveControl=new FlyingMoveControl(this,24,true);
  encounterBar=new ServerBossEvent(Component.literal("Eagle"),BossEvent.BossBarColor.YELLOW,BossEvent.BossBarOverlay.PROGRESS);
 }
 public static AttributeSupplier.Builder attributes(){return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,36).add(Attributes.MOVEMENT_SPEED,.40).add(Attributes.FLYING_SPEED,1.0).add(Attributes.ATTACK_DAMAGE,8).add(Attributes.FOLLOW_RANGE,56);}
 @Override protected PathNavigation createNavigation(Level level){FlyingPathNavigation nav=new FlyingPathNavigation(this,level);nav.setCanOpenDoors(false);nav.setCanFloat(true);nav.setCanPassDoors(true);return nav;}
 @Override protected void registerGoals(){
  goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(1,new SitWhenOrderedToGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1.8,true));goalSelector.addGoal(3,new FollowOwnerGoal(this,1.45,7,2));goalSelector.addGoal(4,new WaterAvoidingRandomFlyingGoal(this,1.25));goalSelector.addGoal(5,new LookAtPlayerGoal(this,Player.class,16));goalSelector.addGoal(6,new RandomLookAroundGoal(this));
  targetSelector.addGoal(1,new HurtByTargetGoal(this).setAlertOthers(EagleEntity.class));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,10,true,false,p->vengeanceSpawned&&!isTame()));
 }
 @Override public boolean doHurtTarget(Entity target){attackAnimationTicks=12;boolean hit=super.doHurtTarget(target);if(hit&&target instanceof LivingEntity l){Vec3 away=l.position().subtract(position()).normalize().scale(.65);l.push(away.x,.12,away.z);}return hit;}
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

 private void updateBossBar(){
  if(!(level() instanceof ServerLevel server)||isTame())return;
  float hp,max;
  if(encounterMembers.isEmpty()){hp=getHealth();max=getMaxHealth();}
  else{hp=0;max=0;for(EagleEntity e:encounterMembers){max+=e.getMaxHealth();if(e.isAlive())hp+=e.getHealth();}}
  encounterBar.setProgress(max<=0?0:Math.max(0,Math.min(1,hp/max)));
  boolean alive=encounterMembers.isEmpty()?isAlive():encounterMembers.stream().anyMatch(Entity::isAlive);
  for(ServerPlayer p:server.players()){
   boolean near=alive && encounterMembers.stream().filter(Entity::isAlive).anyMatch(e->e.distanceToSqr(p)<4096);
   if(encounterMembers.isEmpty())near=alive&&distanceToSqr(p)<4096;
   if(near)encounterBar.addPlayer(p);else encounterBar.removePlayer(p);
  }
  encounterBar.setVisible(alive);
 }

 @Override public void tick(){
  super.tick();if(attackAnimationTicks>0)attackAnimationTicks--;updateBossBar();
  if(!isTame()&&trustReady&&isVehicle()&&getFirstPassenger() instanceof Player rider){
   tamingRideTicks++;setNoGravity(true);fallDistance=0;double targetRise=Math.min(18+(tameProgress*2),28),rise=getY()-tamingStartY;double spiral=(tickCount%80)/80D*Math.PI*2;
   setDeltaMovement(getDeltaMovement().scale(.74).add(Math.cos(spiral)*.05,rise<targetRise?.11:.025,Math.sin(spiral)*.05));
   if(tamingRideTicks>=tamingGoalTicks){tameProgress++;trustReady=false;ejectPassengers();setNoGravity(false);if(tameProgress>=2+random.nextInt(3)){tame(rider);encounterBar.removeAllPlayers();setOrderedToSit(false);level().broadcastEntityEvent(this,(byte)7);if(!rider.addItem(new ItemStack(ModItems.EYE_OF_THE_EAGLE.get())))spawnAtLocation(ModItems.EYE_OF_THE_EAGLE.get());}else level().broadcastEntityEvent(this,(byte)6);}
  }else{setNoGravity(saddled&&isVehicle());if(isVehicle())fallDistance=0;}
 }

 @Override public void travel(Vec3 input){LivingEntity rider=getControllingPassenger();if(isTame()&&saddled&&rider instanceof Player){setYRot(rider.getYRot());yRotO=getYRot();setXRot(rider.getXRot()*.5F);float forward=rider.zza,strafe=rider.xxa*.5F;double vertical=-rider.getXRot()/58D;if(Math.abs(vertical)<.045)vertical=0;double speed=getAttributeValue(Attributes.FLYING_SPEED);Vec3 look=rider.getLookAngle();Vec3 desired=new Vec3(look.x*forward+strafe*Math.cos(Math.toRadians(rider.getYRot())),vertical,look.z*forward+strafe*Math.sin(Math.toRadians(rider.getYRot())));if(desired.lengthSqr()>.001)desired=desired.normalize().scale(speed);setDeltaMovement(getDeltaMovement().scale(.62).add(desired.scale(.38)));move(MoverType.SELF,getDeltaMovement());return;}super.travel(input);}
 @Nullable @Override public LivingEntity getControllingPassenger(){return getFirstPassenger() instanceof LivingEntity l?l:null;}
 @Override protected boolean canAddPassenger(Entity p){return getPassengers().isEmpty()&&p instanceof Player;}
 @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);tag.putBoolean("Saddled",saddled);tag.putBoolean("VengeanceSpawned",vengeanceSpawned);tag.putBoolean("TrustReady",trustReady);tag.putInt("TameProgress",tameProgress);}
 @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);saddled=tag.getBoolean("Saddled");vengeanceSpawned=tag.getBoolean("VengeanceSpawned");trustReady=tag.getBoolean("TrustReady");tameProgress=tag.getInt("TameProgress");}

 @Override public void die(DamageSource source){
  boolean retaliate=!isTame()&&!vengeanceSpawned&&!level().isClientSide;LivingEntity killer=source.getEntity() instanceof LivingEntity l?l:null;
  super.die(source);
  if(retaliate&&level() instanceof ServerLevel server){
   encounterMembers=new ArrayList<>();
   for(int i=0;i<3;i++){EagleEntity eagle=ModEntities.EAGLE.get().create(server);if(eagle!=null){eagle.vengeanceSpawned=true;eagle.encounterBar=this.encounterBar;eagle.encounterMembers=this.encounterMembers;eagle.moveTo(getX()+random.nextInt(9)-4,getY()+6+random.nextInt(5),getZ()+random.nextInt(9)-4,random.nextFloat()*360,0);encounterMembers.add(eagle);server.addFreshEntity(eagle);if(killer!=null)eagle.setTarget(killer);}}
   encounterBar.setProgress(1F);encounterBar.setName(Component.literal("Eagle — Vengeance"));encounterBar.setVisible(true);
  }
 }
 @Nullable @Override public EagleEntity getBreedOffspring(ServerLevel level,AgeableMob other){return ModEntities.EAGLE.get().create(level);}
}
