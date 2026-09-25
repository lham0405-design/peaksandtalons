package com.leigh.peaksandtalons.world;

import com.leigh.peaksandtalons.entity.EagleEntity;
import com.leigh.peaksandtalons.registry.ModBlocks;
import com.leigh.peaksandtalons.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class OrdukHollowBuilder {
 private OrdukHollowBuilder(){}
 public static BlockPos build(ServerLevel level,BlockPos requested){
  int y=Math.max(level.getMinBuildHeight()+24,Math.min(requested.getY(),level.getSeaLevel()-26)); BlockPos c=new BlockPos(requested.getX(),y,requested.getZ());
  BlockState wall=Blocks.DEEPSLATE_BRICKS.defaultBlockState(),trim=Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState(),floor=Blocks.POLISHED_DEEPSLATE.defaultBlockState();
  // 43-block-wide, 22-block-tall mountain cavern: enclosed, but large enough for Eagle circling/dives.
  for(int x=-22;x<=22;x++)for(int z=-22;z<=22;z++){double r=Math.sqrt(x*x+z*z);if(r>22)continue;double roof=19-Math.max(0,r-14)*.55;for(int dy=-2;dy<=21;dy++){BlockPos p=c.offset(x,dy,z);if(dy==-2||r>20.8||dy>roof)level.setBlock(p,randomTrim(x,z,dy)?trim:wall,3);else level.setBlock(p,Blocks.AIR.defaultBlockState(),3);}if(r<=19.5)level.setBlock(c.offset(x,-1,z),floor,3);}
  // Tiered ruins and cover around perimeter.
  for(int ring=15;ring<=18;ring++)for(int x=-ring;x<=ring;x++)for(int z=-ring;z<=ring;z++){double r=Math.sqrt(x*x+z*z);if(r>=ring-.3&&r<=ring+.3)level.setBlock(c.offset(x,ring-14,z),randomTrim(x,z,ring)?trim:wall,3);}
  for(int d=-3;d<=3;d++)for(int h=0;h<=7;h++){level.setBlock(c.offset(d,h,18),Blocks.AIR.defaultBlockState(),3);level.setBlock(c.offset(d,h,-18),Blocks.AIR.defaultBlockState(),3);}
  // Grand descending mountain approach.
  for(int i=0;i<20;i++){int yy=7-(i/3);for(int w=-3;w<=3;w++){BlockPos step=c.offset(w,yy,22+i);level.setBlock(step,Blocks.POLISHED_DEEPSLATE_STAIRS.defaultBlockState(),3);for(int h=1;h<=7;h++)level.setBlock(step.above(h),Blocks.AIR.defaultBlockState(),3);}}
  makeAlcove(level,c.offset(-17,0,10),wall,true);makeAlcove(level,c.offset(17,0,10),wall,false);makeAlcove(level,c.offset(-17,0,-10),wall,false);makeAlcove(level,c.offset(17,0,-10),wall,true);
  // Offering dais. Orduk does NOT exist until the Explorer's Compass is submitted.
  for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++)level.setBlock(c.offset(x,-1,z),Blocks.CHISELED_DEEPSLATE.defaultBlockState(),3);
  level.setBlock(c,ModBlocks.ORDUK_OFFERING_PODIUM.get().defaultBlockState(),3);
  // Arena guardians: aerial Eagles plus spiders. They must be cleared before the podium accepts the compass.
  for(int i=0;i<3;i++){EagleEntity e=ModEntities.EAGLE.get().create(level);if(e!=null){double a=Math.PI*2*i/3;e.moveTo(c.getX()+Math.cos(a)*11+.5,c.getY()+9+i*2,c.getZ()+Math.sin(a)*11+.5,0,0);level.addFreshEntity(e);}}
  for(int i=0;i<5;i++){Spider s=EntityType.SPIDER.create(level);if(s!=null){double a=Math.PI*2*i/5;s.moveTo(c.getX()+Math.cos(a)*14+.5,c.getY(),c.getZ()+Math.sin(a)*14+.5,0,0);level.addFreshEntity(s);}}
  return c;
 }
 private static boolean randomTrim(int x,int z,int y){return Math.floorMod(x*31+z*17+y*13,11)==0;}
 private static void makeAlcove(ServerLevel level,BlockPos p,BlockState wall,boolean food){for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++)for(int y=0;y<=5;y++){BlockPos q=p.offset(x,y,z);if(Math.abs(x)==3||Math.abs(z)==3||y==0)level.setBlock(q,wall,3);else level.setBlock(q,Blocks.AIR.defaultBlockState(),3);}BlockPos chest=p.above();level.setBlock(chest,Blocks.CHEST.defaultBlockState(),3);if(level.getBlockEntity(chest)instanceof Container inv){inv.setItem(0,new ItemStack(food?Items.COOKED_BEEF:Items.IRON_INGOT,food?6:4));inv.setItem(1,new ItemStack(Items.ARROW,12));inv.setItem(2,new ItemStack(food?Items.GOLDEN_CARROT:Items.GOLD_INGOT,food?3:2));}}
 public static BlockPos targetFrom(ServerLevel level,BlockPos origin,Vec3 look){Vec3 flat=new Vec3(look.x,0,look.z);if(flat.lengthSqr()<.01)flat=new Vec3(0,0,1);flat=flat.normalize().scale(56);return new BlockPos(origin.getX()+(int)Math.round(flat.x),origin.getY()-12,origin.getZ()+(int)Math.round(flat.z));}
}
