package com.mydeblob.nosound;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class NoSound extends JavaPlugin implements Listener{

	public boolean isRegistered;
	public void onEnable() {
		register();
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("[NoAnvilSound] Successfully enabled!");
	}

	public void onDisable() {
		unregister();
	}

	public void register(){
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(this, ConnectionSide.SERVER_SIDE, 
						ListenerPriority.NORMAL, Packets.Server.NAMED_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						switch (event.getPacketID()) {
						case Packets.Server.NAMED_SOUND_EFFECT:
							WrapperPlayServerNamedSoundEffect sound = new WrapperPlayServerNamedSoundEffect(
									event.getPacket());

							if ("random.anvil_land".equals(sound.getSoundName())) {
								event.setCancelled(true);
							}
							break;
						}
					}
				});
		this.isRegistered = true;
	}
	
    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        this.isRegistered = false;
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
    	if(e.getBlock().getType() == Material.ANVIL){
    		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ANVIL_LAND, 10f, 1f);
    	}
    }

}
