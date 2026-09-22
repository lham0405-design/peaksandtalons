package com.leigh.peaksandtalons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class NecklaceItem extends Item {
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.literal("+" + chain.armor() + " Armor").withStyle(ChatFormatting.BLUE));
        lines.add(Component.translatable("tooltip.peaksandtalons.socket." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, lines, flag);
    }
}
