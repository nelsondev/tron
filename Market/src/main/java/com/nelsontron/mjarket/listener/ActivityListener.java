package com.nelsontron.mjarket.listener;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;

public class ActivityListener implements Listener {

    Government government;
    public ActivityListener(Government government) {
        this.government = government;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent ev) {
        Consumer consumer;
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = ev.getClickedBlock();
            if (block.getState() instanceof Chest
                    || block.getState() instanceof Barrel
                    || block.getState() instanceof DoubleChest
                    || block.getState() instanceof Hopper
                    || block.getState() instanceof Furnace) {
                consumer = government.getConsumerData().getConsumer(ev.getPlayer().getUniqueId());
                consumer.setActivity(consumer.getActivity()+1);
            }
        }
    }

    private boolean isWood(Block block) {
        boolean isOak = block.getType() == Material.OAK_LOG;
        boolean isAcadia = block.getType() == Material.ACACIA_LOG;
        boolean isBirch = block.getType() == Material.BIRCH_LOG;
        boolean isDark = block.getType() == Material.DARK_OAK_LOG;
        boolean isSpruce = block.getType() == Material.SPRUCE_LOG;
        boolean isJungle = block.getType() == Material.JUNGLE_LOG;

        return isOak || isAcadia || isBirch || isDark || isSpruce || isJungle;
    }

    private boolean isOre(Block block) {
        boolean isDia = block.getType() == Material.DIAMOND_ORE;
        boolean isCoal = block.getType() == Material.COAL_ORE;
        boolean isLapis = block.getType() == Material.LAPIS_ORE;
        boolean isIron = block.getType() == Material.IRON_ORE;
        boolean isGold = block.getType() == Material.GOLD_ORE;
        boolean isEmerald = block.getType() == Material.EMERALD_ORE;
        boolean isNether = block.getType() == Material.NETHER_QUARTZ_ORE;
        boolean isRed = block.getType() == Material.REDSTONE_ORE;

        return isDia || isCoal || isLapis || isIron || isGold || isEmerald;
    }

    private boolean isMisc(Block block) {
        boolean isStoneBrick = block.getType() == Material.STONE_BRICKS;
        boolean isChest = block.getType() == Material.CHEST;
        boolean isFurnace = block.getType() == Material.FURNACE;
        boolean isStone = block.getType() == Material.ANDESITE;
        boolean isStone1 = block.getType() == Material.GRANITE;
        boolean isStone2 = block.getType() == Material.DIORITE;
        boolean isCraft = block.getType() == Material.CRAFTING_TABLE;
        boolean isHopper = block.getType() == Material.HOPPER;
        boolean isWheat = block.getType() == Material.WHEAT;
        boolean isKin = block.getType() == Material.PUMPKIN;
        boolean isPotato = block.getType() == Material.POTATOES;
        boolean isCarrot = block.getType() == Material.CARROTS;

        return isStoneBrick || isChest || isFurnace || isStone || isStone1
                || isStone2 || isCraft || isHopper || isWheat || isKin || isPotato || isCarrot;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent ev) {
        if (ev.getBlock() == null)
            return;

        if (ev.getBlock().getType() == null)
            return;

        Consumer consumer = government.getConsumerData().getConsumer(ev.getPlayer().getUniqueId());
        if (isWood(ev.getBlock()) || isOre(ev.getBlock()) || isMisc(ev.getBlock()))
            consumer.setActivity(consumer.getActivity()+0.05f);

        if (ev.getBlock().getType() == Material.STONE || ev.getBlock().getType() == Material.DIRT) {
            consumer.addActivity(0.1f);
        }
    }

    @EventHandler
    public void craftItem(CraftItemEvent ev) {
        Player player = (Player) ev.getWhoClicked();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+0.5f);
    }

    @EventHandler
    public void enchantItem(EnchantItemEvent ev) {
        Player player = ev.getEnchanter();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+10);
    }

    @EventHandler
    public void fishItem(PlayerFishEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+0.02f);
    }

    @EventHandler
    public void eggThrow(PlayerEggThrowEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+2);
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+1);
        if (isWood(ev.getBlock()) || isOre(ev.getBlock()) || isMisc(ev.getBlock()))
            consumer.setActivity(consumer.getActivity()+0.5f);
    }

    @EventHandler
    public void playerBucket(PlayerBucketFillEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+2);
    }

    @EventHandler
    public void playerDiscoverRecipe(PlayerRecipeDiscoverEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+25);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+100);
    }

    @EventHandler
    public void playerLeash(PlayerLeashEntityEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+2);
    }

    @EventHandler
    public void playerConsume(PlayerItemConsumeEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+5);
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent ev) {
        Player player = ev.getPlayer();
        if (ev.getRightClicked() instanceof Villager || ev.getRightClicked() instanceof Cow) {
            Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
            consumer.setActivity(consumer.getActivity()+5);
        }
    }

    @EventHandler
    public void entityKill(EntityDeathEvent ev) {
        Player player = ev.getEntity().getKiller();

        if (player == null || ev.getEntity().getKiller() == null)
            return;

        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.setActivity(consumer.getActivity()+4);
    }
}
