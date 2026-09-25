package com.leigh.peaksandtalons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Orduk's unique boss drop. Right-click casts the heart forward as a supernatural
 * directional pulse for the Griffin hunt. Q remains ordinary Minecraft item-drop behavior.
 */
public class TrollHeartItem extends Item {
 public TrollHeartItem(Properties properties){super(properties);}
 @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand){
  ItemStack stack=player.getItemInHand(hand);
  if(!(level instanceof ServerLevel sl)||!(player instanceof ServerPlayer sp))return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
  if(sp.getCooldowns().isOnCooldown(this))return InteractionResultHolder.fail(stack);
  Vec3 from=sp.getEyePosition();Vec3 direction=sp.getLookAngle().normalize();
  for(int i=2;i<=42;i++){Vec3 p=from.add(direction.scale(i*.75));sl.sendParticles(i%4==0?ParticleTypes.SOUL_FIRE_FLAME:ParticleTypes.SOUL,p.x,p.y,p.z,2,.10,.10,.10,.01);}
  sl.playSound(null,sp.blockPosition(),SoundEvents.WARDEN_HEARTBEAT,SoundSource.PLAYERS,1.25f,.62f);
  sp.displayClientMessage(Component.literal("The Heart of Orduk pulls toward a greater predator beyond the Hollow...").withStyle(ChatFormatting.DARK_PURPLE),true);
  sp.getCooldowns().addCooldown(this,20*12);
  return InteractionResultHolder.success(stack);
 }
}
