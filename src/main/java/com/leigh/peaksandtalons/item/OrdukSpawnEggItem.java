package com.leigh.peaksandtalons.item;

import com.leigh.peaksandtalons.entity.OrdukEntity;
import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/** Developer/testing spawn egg for quickly exercising the complete Orduk encounter. */
public class OrdukSpawnEggItem extends Item {
    public OrdukSpawnEggItem(Properties properties) {
        super(properties.stacksTo(64));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        OrdukEntity orduk = ModEntities.ORDUK.get().create(server);
        if (orduk == null) return InteractionResult.FAIL;

        orduk.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, context.getRotation(), 0.0F);
        orduk.finalizeSpawn(server, server.getCurrentDifficultyAt(pos), MobSpawnType.SPAWN_EGG, null);
        server.addFreshEntity(orduk);
        if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
