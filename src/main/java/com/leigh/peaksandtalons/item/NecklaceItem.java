package com.leigh.peaksandtalons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.util.List;

public class NecklaceItem extends Item implements ICurioItem {
    private final ChainMaterial chain;
    private final SocketType socket;

    public NecklaceItem(ChainMaterial chain, SocketType socket, Properties properties) {
        super(properties.stacksTo(1));
        this.chain = chain;
        this.socket = socket;
    }

    public ChainMaterial chain() { return chain; }
    public SocketType socket() { return socket; }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity wearer = context.entity();
        if (wearer.level().isClientSide()) return;
        switch (socket) {
            case MAGMA_CREAM -> wearer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 0, true, false, true));
            case PRISMARINE -> wearer.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
            case REDSTONE -> wearer.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 30, 0, true, false, true));
            case EYE_OF_THE_EAGLE -> wearer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 0, true, false, true));
            case DIAMOND -> wearer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 0, true, false, true));
            case EMERALD -> wearer.addEffect(new MobEffectInstance(MobEffects.LUCK, 30, 0, true, false, true));
            case DRAGONS_EGG -> wearer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 1, true, false, true));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.literal("Curios Slot: Necklace").withStyle(ChatFormatting.AQUA));
        lines.add(Component.literal("+" + chain.armor() + " Armor").withStyle(ChatFormatting.BLUE));
        lines.add(Component.translatable("tooltip.peaksandtalons.socket." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, lines, flag);
    }
}
