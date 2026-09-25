package com.leigh.peaksandtalons.block;

import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class OrdukOfferingPodiumBlock extends Block {
 public OrdukOfferingPodiumBlock(Properties p){super(p);}
 @Override protected InteractionResult useWithoutItem(BlockState state,Level level,BlockPos pos,Player player,BlockHitResult hit){
  if(!level.isClientSide) player.displayClientMessage(Component.literal("The podium bears a compass-shaped recess."),true);
  return InteractionResult.SUCCESS;
 }
 @Override protected ItemInteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
  if(!stack.is(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("explorerscompass","explorerscompass")))) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  if(!(level instanceof ServerLevel sl)) return ItemInteractionResult.SUCCESS;
  var nearby=sl.getEntitiesOfClass(Monster.class,new net.minecraft.world.phys.AABB(pos).inflate(24),m->m.isAlive());
  if(!nearby.isEmpty()){player.displayClientMessage(Component.literal("The offering is rejected. Clear the arena guardians first."),true);return ItemInteractionResult.FAIL;}
  if(!player.getAbilities().instabuild) stack.shrink(1);
  Monster boss=ModEntities.ORDUK.get().create(sl);
  if(boss!=null){boss.moveTo(pos.getX()+.5,pos.getY()+1,pos.getZ()-7.5,180,0);sl.addFreshEntity(boss);}
  sl.levelEvent(2001,pos,net.minecraft.world.level.block.Block.getId(net.minecraft.world.level.block.Blocks.CHISELED_DEEPSLATE.defaultBlockState()));
  player.displayClientMessage(Component.literal("The compass sinks into the ancient stone. Something beneath the arena wakes..."),true);
  return ItemInteractionResult.SUCCESS;
 }
}
