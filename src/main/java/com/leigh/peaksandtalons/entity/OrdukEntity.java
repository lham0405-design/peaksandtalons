package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OrdukEntity extends Monster {
 private final ServerBossEvent boss=new ServerBossEvent(Component.literal("ORDUK, THE CAVE TROLL"),BossEvent.BossBarColor.RED,BossEvent.BossBarOverlay.PROGRESS);
 private int specialCooldown=55,attackTell=0,attackKind=0; private boolean stormStarted=false;
 public OrdukEntity(EntityType<? extends Monster> t,Level l){super(t,l);xpReward=120;}
 public static AttributeSupplier.Builder attributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,260).add(Attributes.ATTACK_DAMAGE,14).add(Attributes.ARMOR,12).add(Attributes.ARMOR_TOUGHNESS,4).add(Attributes.MOVEMENT_SPEED,.31).add(Attributes.KNOCKBACK_RESISTANCE,.78).add(Attributes.FOLLOW_RANGE,48);}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1.15,false));goalSelector.addGoal(6,new RandomStrollGoal(this,.8));goalSelector.addGoal(7,new LookAtPlayerGoal(this,Player.class,16));goalSelector.addGoal(8,new RandomLookAroundGoal(this));targetSelector.addGoal(1,new HurtByTargetGoal(this));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));}
 @Override public void startSeenByPlayer(ServerPlayer p){super.startSeenByPlayer(p);boss.addPlayer(p);}@Override public void stopSeenByPlayer(ServerPlayer p){super.stopSeenByPlayer(p);boss.removePlayer(p);}
 @Override public void aiStep(){super.aiStep();if(level().isClientSide)return;boss.setProgress(getHealth()/getMaxHealth());LivingEntity t=getTarget();if(t==null||!t.isAlive())return;getLookControl().setLookAt(t,30,30);double d2=distanceToSqr(t);
  if(!stormStarted&&getHealth()<=getMaxHealth()*.52F){stormStarted=true;if(level() instanceof ServerLevel sl){sl.setWeatherParameters(0,20*90,true,true);for(int i=0;i<4;i++)visualLightning(sl,t,7+i*2);}}
  if(stormStarted&&tickCount%75==0&&level() instanceof ServerLevel sl){double a=random.nextDouble()*Math.PI*2,r=3+random.nextDouble()*5;LightningBolt bolt=EntityType.LIGHTNING_BOLT.create(sl);if(bolt!=null){bolt.moveTo(t.getX()+Math.cos(a)*r,t.getY(),t.getZ()+Math.sin(a)*r);sl.addFreshEntity(bolt);}}
  if(specialCooldown--<=0&&attackTell==0){if(d2>144)attackKind=random.nextInt(3)+3;else attackKind=1+random.nextInt(5);attackTell=attackKind==1?24:16;specialCooldown=getHealth()<getMaxHealth()*.5F?42:62;}
  if(attackTell>0&&--attackTell==0)performSpecial(t,attackKind);
 }
 private void visualLightning(ServerLevel sl,LivingEntity t,double r){double a=random.nextDouble()*Math.PI*2;LightningBolt b=EntityType.LIGHTNING_BOLT.create(sl);if(b!=null){b.moveTo(t.getX()+Math.cos(a)*r,t.getY(),t.getZ()+Math.sin(a)*r);b.setVisualOnly(true);sl.addFreshEntity(b);}}
 private void performSpecial(LivingEntity t,int k){switch(k){
  case 1->{level().levelEvent(2001,blockPosition().below(),net.minecraft.world.level.block.Block.getId(Blocks.STONE.defaultBlockState()));for(LivingEntity e:level().getEntitiesOfClass(LivingEntity.class,new AABB(blockPosition()).inflate(5.5),e->e!=this)){e.hurt(damageSources().mobAttack(this),13);Vec3 a=e.position().subtract(position()).normalize();e.push(a.x*1.1,.55,a.z*1.1);}}
  case 2->{if(distanceToSqr(t)<25){t.hurt(damageSources().mobAttack(this),17);Vec3 a=t.position().subtract(position()).normalize();t.push(a.x*1.5,.35,a.z*1.5);}}
  case 3->{Vec3 d=t.position().subtract(position()).normalize();setDeltaMovement(d.x*1.25,.08,d.z*1.25);hasImpulse=true;if(distanceToSqr(t)<36)t.hurt(damageSources().mobAttack(this),18);}
  case 4->{Vec3 d=t.position().subtract(position()).normalize();setDeltaMovement(d.x*.9,.82,d.z*.9);hasImpulse=true;}
  case 5->{if(hasLineOfSight(t)){level().levelEvent(2001,t.blockPosition(),net.minecraft.world.level.block.Block.getId(Blocks.COBBLESTONE.defaultBlockState()));t.hurt(damageSources().mobAttack(this),12);Vec3 a=t.position().subtract(position()).normalize();t.push(a.x*.75,.28,a.z*.75);}}
 }}
 @Override public boolean doHurtTarget(Entity t){boolean h=super.doHurtTarget(t);if(h&&t instanceof LivingEntity l)l.knockback(1.1F,getX()-l.getX(),getZ()-l.getZ());return h;}
 @Override protected void dropCustomDeathLoot(ServerLevel l,DamageSource s,boolean hit){super.dropCustomDeathLoot(l,s,hit);spawnAtLocation(new ItemStack(ModItems.TROLL_HEART.get()));if(stormStarted)l.setWeatherParameters(20*20,0,false,false);}
}
