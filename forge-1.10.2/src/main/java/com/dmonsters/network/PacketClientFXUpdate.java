package com.dmonsters.network;


import java.util.Random;

import com.dmonsters.main.ModSounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClientFXUpdate implements IMessage {
	
	public static enum Type {
		SOULEYE,
		DUMP,
		WOMAN_HEART,
		SUNLIGHT_USE,
		TIME_CHANGE;		
	    public static Type fromInteger(int x) {
	        switch(x) {
	        case 0:
	            return SOULEYE;
	        case 1:
	            return DUMP;
	        case 2:
	            return WOMAN_HEART;
	        case 3:
	            return SUNLIGHT_USE;
	        case 4:
	            return TIME_CHANGE;
	        }
	        return null;
	    }
	}
	
    private BlockPos blockPos;
    private Type FXtype;
    
    public PacketClientFXUpdate() {
    }

    public PacketClientFXUpdate(BlockPos pos, Type type) {
    	blockPos = pos;
    	FXtype = type;
    	
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        FXtype = FXtype.fromInteger(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        buf.writeInt(FXtype.ordinal());
    }

    public static class Handler implements IMessageHandler<PacketClientFXUpdate, IMessage> {
        @Override
        public IMessage onMessage(PacketClientFXUpdate message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketClientFXUpdate message, MessageContext ctx) {
        	switch (message.FXtype) {
        	case SOULEYE:
        		SoulEye(message, ctx);
        		break;
        	case DUMP:
        		Dump(message, ctx);
        		break;
        	case WOMAN_HEART:
        		WomanHeart(message, ctx);
        		break;
        	case SUNLIGHT_USE:
        		HauntedCowSounds(message, ctx, 0);
        		break;
        	case TIME_CHANGE:
        		HauntedCowSounds(message, ctx, 1);
        		break;
			default:
				break;
        	}
        }
        
        private void SoulEye(PacketClientFXUpdate message, MessageContext ctx) {
        	World world = Minecraft.getMinecraft().theWorld;
        	BlockPos pos = message.blockPos;
        	world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_SOULEYE_KILL, SoundCategory.BLOCKS, 1, 1, false);
        	Random rnd = new Random();
        	for (int i = 0; i < 15; i++) {
				double motionX = rnd.nextGaussian() * 0.001D;
				double motionY = Math.abs(rnd.nextGaussian() * 0.08D);
				double motionZ = rnd.nextGaussian() * 0.001D;
				float randX = rnd.nextFloat();
				float randY = rnd.nextFloat();
				float randZ = rnd.nextFloat();
				world.spawnParticle(EnumParticleTypes.FLAME,
						pos.getX() + randX, 
						pos.getY() + 0.5F + randY, 
						pos.getZ() + randZ,
						motionX,
						motionY,
						motionZ,
						new int[0]);
        	}
        }
        
        private void Dump(PacketClientFXUpdate message, MessageContext ctx) {
        	World world = Minecraft.getMinecraft().theWorld;
        	BlockPos pos = message.blockPos;
        	world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.DUMP_MAKE, SoundCategory.BLOCKS, 1, 1, false);
        }
        
        private void HauntedCowSounds(PacketClientFXUpdate message, MessageContext ctx, int soundType) {
        	World world = Minecraft.getMinecraft().theWorld;
        	BlockPos pos = message.blockPos;
        	switch (soundType) {
        	case 0:
            	world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.SUNLIGHTDROP_USE, SoundCategory.AMBIENT, 1, 1, false);
            	Random rnd = new Random();
            	for (int i = 0; i < 32; i++) {
    				double motionX = rnd.nextGaussian() * 0.001D;
    				double motionY = Math.abs(rnd.nextGaussian() * 0.08D);
    				double motionZ = rnd.nextGaussian() * 0.001D;
    				float randX = rnd.nextFloat() * (rnd.nextBoolean() ? 1 : -1);
    				float randY = rnd.nextFloat() * (rnd.nextBoolean() ? 1 : -1);
    				float randZ = rnd.nextFloat() * (rnd.nextBoolean() ? 1 : -1);
    				world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
    						pos.getX() + randX, 
    						pos.getY() + 1.5F + randY, 
    						pos.getZ() + randZ,
    						motionX,
    						motionY,
    						motionZ,
    						new int[0]);
            	}
            	break;
        	case 1:
            	world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.HAUNTEDCOW_TIMECHANGE, SoundCategory.AMBIENT, 1, 1, false);
        		break;
			default:
				break;
        	}
        }
        
        private void WomanHeart(PacketClientFXUpdate message, MessageContext ctx) {
        	World world = Minecraft.getMinecraft().theWorld;
        	BlockPos pos = message.blockPos;
        	world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.MAIDEN_ATTACK, SoundCategory.BLOCKS, 0.25F, 1, false);
        	Random rnd = new Random();
        	for (int i = 0; i < 8; i++) {
				double motionX = rnd.nextGaussian() * 0.001D;
				double motionY = Math.abs(rnd.nextGaussian() * 0.08D);
				double motionZ = rnd.nextGaussian() * 0.001D;
				float randX = rnd.nextFloat();
				float randY = rnd.nextFloat();
				float randZ = rnd.nextFloat();
				world.spawnParticle(EnumParticleTypes.SPELL_INSTANT,
						pos.getX() + randX, 
						pos.getY() + 0.5F + randY, 
						pos.getZ() + randZ,
						motionX,
						motionY,
						motionZ,
						new int[0]);
        	}
        }
    }
}