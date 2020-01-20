package jdude.autoafk.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class PlayerInfo {
	public EntityPlayerMP player;
	public BlockPos position;
	public boolean isAfk;
	public int afkTime;
	
	public PlayerInfo(EntityPlayerMP player) {
		this.player = player;
		this.position = player.getPosition();
		this.isAfk = false;
	}
	
	public boolean isAfk() {
		if (afkTime >= ModConfig.maxAfkTime * 1200) {
			return true;
		} else {
			return false;
		}
	}
	
}