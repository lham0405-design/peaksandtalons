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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
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

    public static boolean hasSocket(Player player, SocketType socket) {
        ItemStack stack = equippedNecklace(player);
        return stack.getItem() instanceof NecklaceItem necklace && necklace.socket() == socket;
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ItemStack stack = equippedNecklace(player);
        var armor = player.getAttribute(Attributes.ARMOR);
        if (armor != null) armor.removeModifier(ARMOR_ID);

        if (!(stack.getItem() instanceof NecklaceItem necklace)) {
            if (!player.isCreative() && !player.isSpectator() && player.getAbilities().mayfly) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
            return;
        }

        if (armor != null) armor.addTransientModifier(new AttributeModifier(ARMOR_ID, necklace.chain().armor(), AttributeModifier.Operation.ADD_VALUE));
        SocketType socket = necklace.socket();
        if (socket == SocketType.REDSTONE) refresh(player, MobEffects.DIG_SPEED);
        if (socket == SocketType.MAGMA_CREAM) refresh(player, MobEffects.FIRE_RESISTANCE);
        if (socket == SocketType.PRISMARINE) refresh(player, MobEffects.WATER_BREATHING);

        if (socket == SocketType.EYE_OF_THE_EAGLE && !player.getAbilities().mayfly) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        } else if (socket != SocketType.EYE_OF_THE_EAGLE && !player.isCreative() && !player.isSpectator() && player.getAbilities().mayfly) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    /** Diamond socket: silk-touch style drops without modifying the player's real tool. */
    @SubscribeEvent
    public static void blockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof Player player)) return;
        if (hasSocket(player, SocketType.DIAMOND)) {
            var item = event.getState().getBlock().asItem();
            if (item != Items.AIR) {
                event.getDrops().clear();
                event.getDrops().add(new ItemEntity(event.getLevel(), event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, new ItemStack(item)));
                event.setDroppedExperience(0);
            }
        } else if (hasSocket(player, SocketType.EMERALD)) {
            // Balanced Fortune-style bonus: one extra copy of each normal drop 50% of the time.
            if (player.getRandom().nextBoolean()) {
                var extras = event.getDrops().stream().map(drop -> new ItemEntity(event.getLevel(), drop.getX(), drop.getY(), drop.getZ(), drop.getItem().copy())).toList();
                event.getDrops().addAll(extras);
            }
        }
    }

    /** Dragon Egg socket: right-click a block to launch a fireball, with a 2-second cooldown. */
    @SubscribeEvent
    public static void dragonFire(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack necklace = equippedNecklace(player);
        if (!(necklace.getItem() instanceof NecklaceItem item) || item.socket() != SocketType.DRAGONS_EGG) return;
        if (player.getCooldowns().isOnCooldown(item)) return;
        if (!player.level().isClientSide) {
            Vec3 look = player.getLookAngle().normalize();
            SmallFireball fireball = new SmallFireball(player.level(), player, look.scale(1.5D));
            fireball.setPos(player.getX() + look.x, player.getEyeY() - 0.1D, player.getZ() + look.z);
            player.level().addFreshEntity(fireball);
            player.getCooldowns().addCooldown(item, 40);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player && hasSocket(player, SocketType.EYE_OF_THE_EAGLE)) {
            event.setDamageMultiplier(0.0F);
        }
    }

    private static void refresh(Player player, net.minecraft.core.Holder<MobEffect> effect) {
        player.addEffect(new MobEffectInstance(effect, 30, 0, true, false, true));
    }
}
