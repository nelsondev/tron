package buildtools;

import buildtools.exceptions.BuildCommandException;
import buildtools.exceptions.ObjectNotPlayerException;
import buildtools.exceptions.SenderNoPermissionException;
import buildtools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Tools extends JavaPlugin {

    @Override
    public void onEnable() {

        Utils.registerCommands(new ToolsHandler(), this, "build", "bui", "bu", ".");
    }

    @Override
    public void onDisable() {

    }

    // Calculations
    private static class Calc {

        static List<Block> getBlocksBetweenPoints(Build build) throws BuildCommandException {
            List<Block> blocks = new ArrayList<>();

            Block block;
            World world = build.getPos1().getWorld();

            if (build.getPos1() == null || build.getPos2() == null)
                throw new BuildCommandException();

            int x1 = build.getPos1().getBlockX();
            int x2 = build.getPos2().getBlockX();
            int xMin = (x1 < x2) ? x1 : x2;
            int xMax = (x1 > x2) ? x1 : x2;

            int y1 = build.getPos1().getBlockY();
            int y2 = build.getPos2().getBlockY();
            int yMin = (y1 < y2) ? y1 : y2;
            int yMax = (y1 > y2) ? y1 : y2;

            int z1 = build.getPos1().getBlockZ();
            int z2 = build.getPos2().getBlockZ();
            int zMin = (z1 < z2) ? z1 : z2;
            int zMax = (z1 > z2) ? z1 : z2;

            for (int x = xMin; x <= xMax; x++) {

                for (int y = yMin; y <= yMax; y++) {

                    for (int z = zMin; z <= zMax; z++) {

                        try {
                            block = world.getBlockAt(x, y, z);

                        } catch (NullPointerException ex) {
                            continue;
                        }

                        blocks.add(block);
                    }
                }
            }

            return blocks;
        }
    }

    private class Data {
        private List<Build> tasks = new ArrayList<>();

        List<Build> getTasks() {
            return tasks;
        }

        Build getPlayerBuild(UUID id) {
            Build build = null;

            for (Build b : tasks) {

                if (b.getId() == id) {
                    build = b;
                }
            }

            return build;
        }
    }

    private class Build {

        Location pos1;
        Location pos2;
        UUID id;

        Build(UUID id) {
            this.pos1 = null;
            this.pos2 = null;
            this.id = id;
        }

        Location getPos1() {
            return pos1;
        }

        Location getPos2() {
            return pos2;
        }

        UUID getId() {
            return id;
        }

        void setPos1(Location location) {
            pos1 = location;
        }

        void setPos2(Location location) {
            pos2 = location;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }

    private class ToolsHandler implements CommandExecutor {

        Data data = new Data();

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Player player;
            String subCommand;
            String message = null;
            Build build;

            try {

                // check if player has permission to use this command.
                Utils.checkPermission(command.getLabel() + ".use", sender);

                // cast player from command sender.
                player = Utils.castPlayer(sender);

                if (args.length == 0) {
                    // TODO: display build command help
                } else {

                    subCommand = args[0];

                    if (subCommand.equalsIgnoreCase("pos")) {
                        build = data.getPlayerBuild(player.getUniqueId());

                        // create new build if it doesn't exist already.
                        if (build == null)
                            build = new Build(player.getUniqueId());

                        if (build.getPos1() == null) {
                            build.setPos1(player.getLocation());
                            build.setPos2(player.getTargetBlock(null, 128).getLocation());

                            message = Utils.message("Set first position to " + build.getPos1().getBlockX()
                                    + ", " + build.getPos1().getBlockY() + ", " + build.getPos1().getBlockZ());
                            message += "\n" + Utils.message("Set second position to cursor position at " + build.getPos2().getBlockX()
                                    + ", " + build.getPos2().getBlockY() + ", " + build.getPos2().getBlockZ());
                        } else {
                            throw new BuildCommandException(BuildCommandException.BuildHelp.CLEAR);
                        }

                        if (!data.getTasks().contains(build))
                            data.getTasks().add(build);
                    } else if (subCommand.equalsIgnoreCase("set")) {

                        if (args.length == 1)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.SETTING);

                        String blockName = Utils.arrayToString(Arrays.copyOfRange(args, 1, args.length), " ").trim();

                        Material type = Material.getMaterial(blockName.replace(" ", "_").toUpperCase());

                        if (type == null)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.BLOCK);

                        build = data.getPlayerBuild(player.getUniqueId());

                        // create new build if it doesn't exist already.
                        if (build == null)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.BUILD);

                        List<Block> blocks = Calc.getBlocksBetweenPoints(build);

                        int count = 0;
                        int missedBlocks = 0;
                        for (Block block : blocks) {
                            if (block.getType() != type) {
                                if (player.getInventory().contains(type)) {
                                    block.setType(type);
                                    player.getInventory().removeItem(new ItemStack(type, 1));
                                    count++;
                                } else
                                    missedBlocks++;
                            }
                        }

                        if (count != 0)
                            message = Utils.message("Set " + count + " blocks to " + blockName.replace("_", " "));
                        if (missedBlocks != 0)
                            message += (count == 0) ? "\n" + Utils.errorMessage("You didn't have enough in your inventory to replace " + missedBlocks + " blocks.")
                                    : Utils.errorMessage("You didn't have enough in your inventory to replace " + missedBlocks + " blocks.");
                        if (count == 0 && missedBlocks == 0)
                            message = Utils.errorMessage("Nothing to do");

                    } else if (subCommand.equalsIgnoreCase("replace")) {

                        if (args.length < 2)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.REPLACE);

                        build = data.getPlayerBuild(player.getUniqueId());

                        // create new build if it doesn't exist already.
                        if (build == null)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.BUILD);

                        String[] blockNames;
                        Material[] type = new Material[2];

                        blockNames = Utils.arrayToString(Arrays.copyOfRange(args, 1, args.length), " ").split("with");

                        if (blockNames.length != 2)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.REPLACE);

                        blockNames[0] = blockNames[0].trim();
                        blockNames[1] = blockNames[1].trim();

                        type[0] = Material.getMaterial(blockNames[0].replace(" ", "_").toUpperCase());
                        type[1] = Material.getMaterial(blockNames[1].replace(" ", "_").toUpperCase());

                        if (type[0] == null || type[1] == null)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.BLOCK);

                        List<Block> blocks = Calc.getBlocksBetweenPoints(build);

                        int count = 0;
                        int missedBlocks = 0;
                        for (Block block : blocks) {
                            if (block.getType() == type[0]) {
                                if (player.getInventory().contains(type[1])) {
                                    player.getInventory().removeItem(new ItemStack(type[1], 1));

                                    for (ItemStack drop : block.getDrops()) {
                                        player.getInventory().addItem(drop);
                                    }
                                    block.setType(type[1]);
                                    count++;
                                } else
                                    missedBlocks++;
                            }
                        }

                        if (count != 0)
                            message = Utils.message("Replaced " + count + " " + blockNames[0] + " blocks with " + blockNames[1]) + "\n";
                        if (missedBlocks != 0)
                            message += (count == 0) ? "\n" + Utils.errorMessage("You didn't have enough in your inventory to replace " + missedBlocks + " blocks.")
                                    : Utils.errorMessage("You didn't have enough in your inventory to replace " + missedBlocks + " blocks.");
                        if (count == 0 && missedBlocks == 0)
                            message = Utils.errorMessage("Nothing to do");

                    } else if (subCommand.equalsIgnoreCase("clear")) {
                        build = data.getPlayerBuild(player.getUniqueId());

                        if (build == null)
                            throw new BuildCommandException(BuildCommandException.BuildHelp.BUILD);

                        data.getTasks().remove(build);

                        message = Utils.message("Cleared your current build");
                    }
                }
            } catch (SenderNoPermissionException | ObjectNotPlayerException | BuildCommandException ex) {
                message = ex.getMessage();
            } finally {
                if (message != null)
                    sender.sendMessage(message);
            }

            return false;
        }
    }
}