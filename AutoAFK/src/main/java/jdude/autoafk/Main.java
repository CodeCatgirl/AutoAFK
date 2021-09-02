package jdude.autoafk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import jdude.autoafk.commands.CommandAfk;
import jdude.autoafk.utils.ColorH;
import jdude.autoafk.utils.PlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Main.MODID, useMetadata = true, acceptableRemoteVersions = "*", serverSideOnly = true)
public class Main {
	public static final String MODID = "autoafk";
	public static final Map<UUID, PlayerInfo> PLAYER_INFOS = new HashMap<UUID, PlayerInfo>();
	
	@Instance
	public static Main mod;
	
	@EventHandler
	@SideOnly(Side.SERVER)
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandAfk());
	}
	
	@SubscribeEvent
	public void onPlayerConnect(PlayerLoggedInEvent connected) {
		PLAYER_INFOS.put(connected.player.getGameProfile().getId(), new PlayerInfo((EntityPlayerMP) connected.player));
	}
	
	@SubscribeEvent
	public void onPlayerDisconect(PlayerLoggedOutEvent disconnected) {
		for (Entry<UUID, PlayerInfo> info : PLAYER_INFOS.entrySet()) {
			if (info.getKey() == disconnected.player.getGameProfile().getId()) {
				PLAYER_INFOS.remove(info.getKey());
				break;
			}
		}
	}
	
	@SubscribeEvent
	public void chatty(ServerChatEvent chat) {
		PlayerInfo info = PLAYER_INFOS.get(chat.getPlayer().getGameProfile().getId());
		if (info == null) {
			return;
		}
		info.afkTime = 0;
		if (info.isAfk) {
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.sendMessage(new TextComponentString(ColorH.addColor("&e" + info.player.getDisplayNameString() + " ya no esta AFK.")));
			info.isAfk = false;
		}
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Iterator<Entry<UUID, PlayerInfo>> it = PLAYER_INFOS.entrySet().iterator();
			while (it.hasNext()) {
				Entry<UUID, PlayerInfo> info = it.next();
				if (info.getValue().player.getPosition().equals(info.getValue().position)) {
					//AFK
					if (info.getValue().isAfk() && !info.getValue().isAfk) {
						info.getValue().isAfk = true;
						FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
								.sendMessage(new TextComponentString(ColorH.addColor("&e" + info.getValue().player.getDisplayNameString() + " ahora esta AFK.")));
					} else {
						info.getValue().afkTime++;
					}
				} else {
					//Not AFk
					info.getValue().position = info.getValue().player.getPosition();
					info.getValue().afkTime = 0;
					if (info.getValue().isAfk) {
						FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
								.sendMessage(new TextComponentString(ColorH.addColor("&e" + info.getValue().player.getDisplayNameString() + " ya no esta AFK.")));
					}
					info.getValue().isAfk = false;
					
				}
			}
		}
	}
}
