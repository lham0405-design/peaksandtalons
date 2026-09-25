package com.leigh.peaksandtalons.world;

import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Builds the first playable Orduk's Hollow encounter entirely server-side.
 *  The hollow is deliberately authored as a dungeon: approach stair, antechamber,
 *  side loot alcoves and a sunken circular boss floor with tiered spectator ruins.
 */
public final class OrdukHollowBuilder {
    private OrdukHollowBuilder() {}

    public static BlockPos build(ServerLevel level, BlockPos requested) {
        int y = Math.max(level.getMinBuildHeight() + 18, Math.min(requested.getY(), level.getSeaLevel() - 18));
        BlockPos c = new BlockPos(requested.getX(), y, requested.getZ());
        BlockState wall = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        BlockState trim = Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
        BlockState floor = Blocks.POLISHED_DEEPSLATE.defaultBlockState();

        // Main vaulted arena: 31 blocks wide, 9 blocks high, circular footprint.
        for (int x=-16;x<=16;x++) for(int z=-16;z<=16;z++) {
            double r=Math.sqrt(x*x+z*z);
            if(r>16) continue;
            for(int dy=-2;dy<=9;dy++) {
                BlockPos p=c.offset(x,dy,z);
                if(dy==-2 || r>14.8) level.setBlock(p, (randomTrim(x,z,dy)?trim:wall), 3);
                else level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            }
            if(r<=13.5) level.setBlock(c.offset(x,-1,z), floor,3);
        }

        // Two-tier ruined seating / combat cover around the perimeter.
        for(int ring=11;ring<=13;ring++) for(int x=-ring;x<=ring;x++) for(int z=-ring;z<=ring;z++) {
            double r=Math.sqrt(x*x+z*z);
            if(r>=ring-.35 && r<=ring+.35) {
                int h=ring-10;
                level.setBlock(c.offset(x,h-1,z), randomTrim(x,z,h)?trim:wall,3);
            }
        }
        // Break deliberate gaps so the tiers remain navigable.
        for(int d=-2;d<=2;d++) for(int h=0;h<=4;h++) {
            level.setBlock(c.offset(d,h,12),Blocks.AIR.defaultBlockState(),3);
            level.setBlock(c.offset(d,h,-12),Blocks.AIR.defaultBlockState(),3);
        }

        // Grand north approach: descending 5-wide stair and tunnel into the arena.
        for(int i=0;i<15;i++) {
            int yy=5-(i/3);
            for(int w=-2;w<=2;w++) {
                BlockPos step=c.offset(w,yy,16+i);
                level.setBlock(step, Blocks.POLISHED_DEEPSLATE_STAIRS.defaultBlockState(),3);
                for(int h=1;h<=5;h++) level.setBlock(step.above(h),Blocks.AIR.defaultBlockState(),3);
            }
        }

        // Loot alcoves before the boss floor.
        makeAlcove(level,c.offset(-12,0,8),wall,true);
        makeAlcove(level,c.offset(12,0,8),wall,false);
        makeAlcove(level,c.offset(-12,0,-8),wall,false);
        makeAlcove(level,c.offset(12,0,-8),wall,true);

        // Central altar and Orduk. The arena itself is the trigger for this first playable pass.
        level.setBlock(c.below(),Blocks.CHISELED_DEEPSLATE.defaultBlockState(),3);
        Monster boss=ModEntities.ORDUK.get().create(level);
        if(boss!=null){boss.moveTo(c.getX()+.5,c.getY(),c.getZ()+.5,0,0);level.addFreshEntity(boss);}
        return c;
    }

    private static boolean randomTrim(int x,int z,int y){return Math.floorMod(x*31+z*17+y*13,11)==0;}

    private static void makeAlcove(ServerLevel level,BlockPos p,BlockState wall,boolean food){
        for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++)for(int y=0;y<=4;y++){
            BlockPos q=p.offset(x,y,z);
            if(Math.abs(x)==3||Math.abs(z)==3||y==0) level.setBlock(q,wall,3); else level.setBlock(q,Blocks.AIR.defaultBlockState(),3);
        }
        BlockPos chest=p.above(); level.setBlock(chest,Blocks.CHEST.defaultBlockState(),3);
        if(level.getBlockEntity(chest) instanceof Container inv){
            inv.setItem(0,new ItemStack(food?Items.COOKED_BEEF:Items.IRON_INGOT,food?6:4));
            inv.setItem(1,new ItemStack(Items.ARROW,12));
            inv.setItem(2,new ItemStack(food?Items.GOLDEN_CARROT:Items.GOLD_INGOT,food?3:2));
        }
    }

    public static BlockPos targetFrom(ServerLevel level, BlockPos origin, Vec3 look){
        Vec3 flat=new Vec3(look.x,0,look.z);
        if(flat.lengthSqr()<.01) flat=new Vec3(0,0,1);
        flat=flat.normalize().scale(56);
        return new BlockPos(origin.getX()+(int)Math.round(flat.x), origin.getY()-12, origin.getZ()+(int)Math.round(flat.z));
    }
}
