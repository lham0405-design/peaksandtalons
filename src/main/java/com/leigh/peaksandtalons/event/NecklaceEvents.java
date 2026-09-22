package com.leigh.peaksandtalons.event;

import com.leigh.peaksandtalons.PeaksAndTalons;
import com.leigh.peaksandtalons.item.NecklaceItem;
import com.leigh.peaksandtalons.item.SocketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = PeaksAndTalons.MOD_ID)
public final class NecklaceEvents {
    private static final ResourceLocation ARMOR_ID = ResourceLocation.fromNamespaceAndPath(PeaksAndTalons.MOD_ID, "necklace_armor");

    public static ItemStack equippedNecklace(Player player) {
        return CuriosApi.getCuriosInventory(player)
            .flatMap(h -> h.findFirstCurio(stack -> stack.getItem() instanceof NecklaceItem))
            .map(r -> r.stack()).orElse(ItemStack.EMPTY);
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ItemStack stack = equippedNecklace(player);
        var armor = player.getAttribute(Attributes.ARMOR);
        if (armor != null) armor.removeModifier(ARMOR_ID);
        if (!(stack.getItem() instanceof NecklaceItem necklace)) return;

        if (armor != null) armor.addTransientModifier(new AttributeModifier(ARMOR_ID, necklace.chain().armor(), AttributeModifier.Operation.ADD_VALUE));
        SocketType socket = necklace.socket();
        if (socket == SocketType.REDSTONE) refresh(player, MobEffects.DIG_SPEED);
        if (socket == SocketType.MAGMA_CREAM) refresh(player, MobEffects.FIRE_RESISTANCE);
        if (socket == SocketType.PRISMARINE) refresh(player, MobEffects.WATER_BREATHING);
    }

    private static void refresh(Player player, net.minecraft.core.Holder<MobEffect> effect) {
        player.addEffect(new MobEffectInstance(effect, 30, 0, true, false, true));
    }
}
