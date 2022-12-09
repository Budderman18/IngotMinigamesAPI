package com.budderman18.IngotMinigamesAPI.Core;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * 
 * This class spawns in fake players
 * It is not functional yet, don't use
 * Unlikely to be finished on first release, as its an extremly confusing
 * system to work with. Eventually there *may* be a way to convert this into a
 * HumanEntity, allowing much better management for the player you make
 * 
 */
@Deprecated
public class FakePlayer extends EntityPlayer {

    private Location loc;

    public FakePlayer(WorldServer ws, GameProfile gp, Location loc) {
        super(MinecraftServer.getServer(), ws, gp, null);
        this.loc = loc;
        //location(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); // set location
    }

    public void spawn() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            spawnFor(pl); // send all spawn packets
        }
    }

    public void spawnFor(Player p) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().connection;
        // add player in player list for player
        connection.send(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this));
        // make player spawn in world
        connection.send(new PacketPlayOutNamedEntitySpawn(this));
        // change head rotation
        connection.send(new PacketPlayOutEntityHeadRotation(this, (byte) ((loc.getYaw() * 256f) / 360f)));
        // now remove player from tab list
        connection.send(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this));
        // here the entity is showed, you can show item in hand like that :;
    }

    public void remove() {
        this.die(DamageSource.OUT_OF_WORLD);
    }

    public boolean isEntity(Entity et) {
        return this.getId() == et.getEntityId(); // check if it's this entity
    }

    public static FakePlayer createNPC(Location loc, String name) {
        // get NMS world
        WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        GameProfile profile = new GameProfile(UUID.randomUUID(), name); // create game profile
        // use class given just before
        FakePlayer ep = new FakePlayer(nmsWorld, profile, loc);
        // now quickly made player connection
        ep.connection = new PlayerConnection(MinecraftServer.getServer(), new NetworkManager(EnumProtocolDirection.values()[0]),ep);
        nmsWorld.addFreshEntity(ep, SpawnReason.CUSTOM); // add entity to world
        ep.spawn(); // spawn for actual online players
        // now you can keep the FakePlayer instance for next player or just to check
        return ep;
    }
    /**
     * 
     * This method converts a FakePlayer into HumanEntity
     * Use this everytime you make a fakeplayer. Working with this class will always be a headache
     * convertToBukkitEntity(createNPC()) should actually be the only line you'll ever need to use
     * for this class, but others do exist if needed.
     * 
     * @param player
     * @return 
     */
    public static HumanEntity convertToBukkitEntity(FakePlayer player) {
        HumanEntity entity = null;
        entity = (HumanEntity) player;
        return entity;
    }
}
