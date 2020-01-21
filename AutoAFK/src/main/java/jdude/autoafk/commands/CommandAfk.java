package jdude.autoafk.commands;

import jdude.autoafk.Main;
import jdude.autoafk.utils.ColorH;
import jdude.autoafk.utils.ModConfig;
import jdude.autoafk.utils.PlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandAfk extends CommandBase {
	
	@Override
	public String getName() {
		return "afk";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "Marks the user as AFK.";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		PlayerInfo info = Main.PLAYER_INFOS.get(((EntityPlayerMP) sender).getGameProfile().getId());
		
		if (info.isAfk) {
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.sendMessage(new TextComponentString(ColorH.addColor("&e" + info.player.getDisplayNameString() + " is no longer AFK.")));
			info.isAfk = false;
			info.afkTime = 0;
		} else {
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.sendMessage(new TextComponentString(ColorH.addColor("&e" + info.player.getDisplayNameString() + " is now AFK.")));
			info.isAfk = true;
			info.afkTime = ModConfig.maxAfkTime * 1200;
		}
	}
	
}
