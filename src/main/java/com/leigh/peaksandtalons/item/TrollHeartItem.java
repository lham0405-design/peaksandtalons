package com.leigh.peaksandtalons.item;

import com.leigh.peaksandtalons.world.OrdukHollowBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

/** The Troll Heart is a progression key, not a throwable Q-drop.
 * Right click reveals/builds Orduk's Hollow in the direction the player is facing
 * and paints a visible soul trail toward its entrance.
 */
public class TrollHeartItem extends Item {
    public TrollHeartItem(Properties properties){super(properties);}

    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        ItemStack stack=player.getItemInHand(hand);
        if(!(level instanceof ServerLevel sl) || !(player instanceof ServerPlayer sp)) return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
        if(sp.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);

        BlockPos target=OrdukHollowBuilder.targetFrom(sl,sp.blockPosition(),sp.getLookAngle());
        BlockPos arena=OrdukHollowBuilder.build(sl,target);
        Vec3 from=sp.getEyePosition();
        Vec3 to=Vec3.atCenterOf(arena.offset(0,5,28));
        Vec3 delta=to.subtract(from);
        for(int i=1;i<=32;i++){
            Vec3 p=from.add(delta.scale(i/32.0));
            sl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,p.x,p.y,p.z,2,.12,.12,.12,.01);
        }
        sl.playSound(null,sp.blockPosition(),SoundEvents.WARDEN_HEARTBEAT,SoundSource.PLAYERS,1.2f,.7f);
        sp.displayClientMessage(Component.literal("The heart beats toward a hollow beneath the mountain...").withStyle(ChatFormatting.DARK_GREEN),true);
        sp.getCooldowns().addCooldown(this,20*30);
        return InteractionResultHolder.success(stack);
    }
}
