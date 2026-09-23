package com.leigh.peaksandtalons.item;

import com.leigh.peaksandtalons.entity.EagleEntity;
import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/** Explicit spawn egg used for alpha/final testing of the custom eagle entity. */
public class EagleSpawnEggItem extends Item {
    public EagleSpawnEggItem(Properties properties) {
        super(properties.stacksTo(64));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        EagleEntity eagle = ModEntities.EAGLE.get().create(server);
        if (eagle == null) return InteractionResult.FAIL;

        eagle.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, context.getRotation(), 0.0F);
        eagle.finalizeSpawn(server, server.getCurrentDifficultyAt(pos), MobSpawnType.SPAWN_EGG, null);
        server.addFreshEntity(eagle);
        if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
