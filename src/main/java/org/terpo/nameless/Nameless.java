package org.terpo.nameless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Nameless.MODID, version = Nameless.VERSION)
public class Nameless {
	public static final String MODID = "nameless";
	public static final String MODNAME = "Nameless Utils";
	public static final String VERSION = "1.0.1";
	public static Configuration config;
	public static boolean setSpawnDay;

	public static Logger LOGGER = LogManager.getLogger(Nameless.MODNAME);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		setSpawnDay = config.get("Settings", "SetSpawnAnyTime", true).getBoolean();
		config.save();
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event) {
		event.setResult(net.minecraft.entity.player.EntityPlayer.SleepResult.OTHER_PROBLEM);
		final World world = event.getEntityPlayer().getEntityWorld();
		final boolean isDayTime = world.isDaytime();
		if ((isDayTime) && (!setSpawnDay)) {
			return;
		}
		if ((world.getBiomeForCoordsBody(event.getPos()) != Biomes.HELL)) {
			final EntityPlayer entityPlayer = event.getEntityPlayer();
			entityPlayer.setSpawnPoint(event.getPos(), false);
			entityPlayer.setSpawnChunk(event.getPos(), false, entityPlayer.dimension);
			if (isDayTime) {
				entityPlayer.sendMessage(new TextComponentTranslation("nameless.spawnpoint.set"));
			} else {
				entityPlayer.sendMessage(new TextComponentTranslation("nameless.spawnpoint.setAtNight"));
			}
		}
	}
}
