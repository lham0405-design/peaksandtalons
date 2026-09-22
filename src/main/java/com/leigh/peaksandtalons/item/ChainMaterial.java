package com.leigh.peaksandtalons.item;

public enum ChainMaterial {
    GOLD(0.5D),
    IRON(1.0D),
    NETHERITE(2.0D);

    private final double armor;
    ChainMaterial(double armor) { this.armor = armor; }
    public double armor() { return armor; }
}
