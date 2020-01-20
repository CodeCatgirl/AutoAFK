package jdude.autoafk.utils;

import jdude.autoafk.Main;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Main.MODID)
@Config.LangKey(Main.MODID + ".config.title")
public class ModConfig {
	@Config.Comment("How long someone must remain AFK before they are marked as such.")
	public static int maxAfkTime = 10;
	
	@EventBusSubscriber(modid = Main.MODID)
	private static class ConfigSync {
		@SubscribeEvent
		public static void onConfigSync(ConfigChangedEvent event) {
			if (event.getModID().equals(Main.MODID)) {
				ConfigManager.sync(Main.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
