package com.leigh.peaksandtalons.entity;

import com.leigh.peaksandtalons.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Orduk is deliberately a mechanics-first boss: heavy telegraphs, high pressure, and punish windows. */
public class OrdukEntity extends Monster {
    private final ServerBossEvent boss = new ServerBossEvent(Component.literal("Orduk, the Cave Troll"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private int specialCooldown = 55;
    private int attackTell = 0;
    private int attackKind = 0;
    private boolean stormStarted = false;

    public OrdukEntity(EntityType<? extends Monster> type, Level level) { super(type, level); xpReward = 120; }

    public static AttributeSupplier.Builder attributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 260.0D)
            .add(Attributes.ATTACK_DAMAGE, 14.0D)
            .add(Attributes.ARMOR, 12.0D)
            .add(Attributes.ARMOR_TOUGHNESS, 4.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.31D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.78D)
            .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, false));
        goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override public void startSeenByPlayer(ServerPlayer player) { super.startSeenByPlayer(player); boss.addPlayer(player); }
    @Override public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); boss.removePlayer(player); }

    @Override public void aiStep() {
        super.aiStep();
        if (!level().isClientSide) {
            boss.setProgress(getHealth() / getMaxHealth());
            LivingEntity target = getTarget();
            if (target == null || !target.isAlive()) return;
            getLookControl().setLookAt(target, 30F, 30F);
            double d2 = distanceToSqr(target);
            if (!stormStarted && getHealth() <= getMaxHealth() * 0.52F) {
                stormStarted = true;
                level().setThunderLevel(1.0F);
                level().setRainLevel(1.0F);
                if (level() instanceof net.minecraft.server.level.ServerLevel sl) sl.setWeatherParameters(0, 20 * 90, true, true);
            }
            if (specialCooldown-- <= 0 && attackTell == 0) {
                if (d2 > 144) attackKind = random.nextBoolean() ? 3 : 4; // charge/leap at range
                else attackKind = 1 + random.nextInt(4); // slam/backhand/charge/leap
                attackTell = attackKind == 1 ? 24 : 16;
                specialCooldown = getHealth() < getMaxHealth() * .5F ? 42 : 62;
            }
            if (attackTell > 0 && --attackTell == 0) performSpecial(target, attackKind);
        }
    }

    private void performSpecial(LivingEntity target, int kind) {
        switch (kind) {
            case 1 -> { // ground slam
                level().levelEvent(2001, blockPosition().below(), net.minecraft.world.level.block.Block.getId(Blocks.STONE.defaultBlockState()));
                for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, new AABB(blockPosition()).inflate(5.5), e -> e != this)) {
                    e.hurt(damageSources().mobAttack(this), 13F);
                    Vec3 away = e.position().subtract(position()).normalize(); e.push(away.x * 1.1, .55, away.z * 1.1);
                }
            }
            case 2 -> { // sweeping backhand
                if (distanceToSqr(target) < 25) { target.hurt(damageSources().mobAttack(this), 17F); Vec3 a=target.position().subtract(position()).normalize(); target.push(a.x*1.5,.35,a.z*1.5); }
            }
            case 3 -> { // explosive troll charge
                Vec3 dir = target.position().subtract(position()).normalize(); setDeltaMovement(dir.x * 1.25, .08, dir.z * 1.25); hasImpulse = true;
                if (distanceToSqr(target) < 36) target.hurt(damageSources().mobAttack(this), 18F);
            }
            case 4 -> { // leap/crash
                Vec3 dir = target.position().subtract(position()).normalize(); setDeltaMovement(dir.x * .9, .82, dir.z * .9); hasImpulse = true;
            }
        }
    }

    @Override public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) living.knockback(1.1F, getX()-living.getX(), getZ()-living.getZ());
        return hit;
    }

    @Override protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        spawnAtLocation(new ItemStack(ModItems.TROLL_HEART.get()));
        if (stormStarted) level.setWeatherParameters(20 * 20, 0, false, false);
    }
}
