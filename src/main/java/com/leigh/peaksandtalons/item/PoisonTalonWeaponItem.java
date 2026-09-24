package com.leigh.peaksandtalons.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/** Eagle-talon weapon with otherwise conventional melee balance. */
public class PoisonTalonWeaponItem extends SwordItem {
    public PoisonTalonWeaponItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        if (!target.level().isClientSide()) {
            // Poison I for 4 seconds. Repeated hits refresh rather than stack the amplifier.
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0), attacker);
        }
    }
}
