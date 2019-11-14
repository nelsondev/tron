package com.nelsontron.siege.commands;

import com.nelsontron.siege.data.GameCreator;
import com.nelsontron.siege.game.Arena;
import com.nelsontron.siege.game.Game;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    private Holder holder;

    public GameCommand(Holder holder) {
        this.holder = holder;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Game game;
        GameCreator creator;
        Arena arena;

        String arenaName;
        int time;

        String subCommand;
        String result = null;

        try {

            player = (Player) sender;
            game = holder.getGame();
            creator = holder.getCreator(player);

            if (args.length == 0) {
                result = Utils.formatSiegeMessage("Please specify a sub command.");
                return true;
            }

            subCommand = args[0];

            /*
             * START SIEGE SETUP SCRIPT
             * siege needs to create a specific SiegeGame object or interface "Game" for it to work.
             * this setup script builds a SiegeGame object through multiple commands to not make it confusing.
             */
            if (creator != null) {

                if (subCommand.equalsIgnoreCase("quit")) {
                    holder.clearGameCreator(creator);
                    player.sendMessage(Utils.formatSiegeMessage("Quit siege setup script."));
                    return true;
                }

                // set game holder
                creator.setHolder(holder);

                if (creator.getArena() == null) {
                    arenaName = Utils.stringify(args, " ");
                    arena = holder.getArena(arenaName);

                    if (arena == null) {
                        player.sendMessage(Utils.formatSiegeMessage("That arena doesn't appear to be loaded into memory. Type /game quit to cancel this script."));
                        return true;
                    }

                    creator.setArena(arena);
                    player.sendMessage(Utils.formatSiegeMessage("Arena " + arenaName + " found and set! Please specify the max time that you'd like this game to take. Type the max time(in seconds) with /game <time>."));
                }
                else if (creator.getTime() == 0) {

                    time = Integer.parseInt(subCommand);
                    creator.setTime(time);

                    game = creator.toGame();
                    holder.clearGameCreator(creator);
                    holder.setGame(game);
                    player.sendMessage(Utils.formatSiegeMessage("Siege setup script finished. Game starting..."));
                    game.start(5);
                }

                return true;
            }

            /*
             * START NORMAL SUB COMMANDS
             * commands like /game start, /game stop, and /game reset. Used AFTER siege setup scripting.
             */
            if (subCommand.equalsIgnoreCase("start")) {

                if (game != null) {
                    if (!game.getState().equalsIgnoreCase("stopped")) {
                        result = Utils.formatSiegeMessage("There's already a game going.");
                        return true;
                    }

                    game.start(5);
                    player.sendMessage(Utils.formatSiegeMessage("Starting siege game with settings from old game. To clear these game settings do /game clear."));
                }
                else {

                    holder.createGameCreator(player);
                    result = Utils.formatSiegeMessage("Starting setup for siege game...\n");
                    result += Utils.formatSiegeMessage("Please specify the arena you'd like this game to reside in. To do this just type /game <arena name>. If this was a mistake you can quit this script by typing /game quit", "quit");
                }
            }
            else if (subCommand.equalsIgnoreCase("stop")) {

                if (game == null) {
                    result = Utils.formatSiegeMessage("There's no game going. Try /game start.");
                    return true;
                }

                if (!game.getState().equalsIgnoreCase("playing")) {
                    result = Utils.formatSiegeMessage("The game currently is unable to be stopped. Needed game state: playing, Current game state: " + game.getState());
                    return true;
                }

                game.setTime(12); // set game to finish safely
                result = Utils.formatSiegeMessage("Stopping game...\n");
            }
            else if (subCommand.equalsIgnoreCase("reset")) {

                if (game == null) {
                    result = Utils.formatSiegeMessage("There's no game going. Try /game start.");
                    return true;
                }

                if (!game.getState().equalsIgnoreCase("playing")) {
                    result = Utils.formatSiegeMessage("The game currently is unable to be reset. Needed game state: playing, Current game state: " + game.getState());
                    return true;
                }

                game.reset();
                result = Utils.formatSiegeMessage("Resetting game...");
            }
            else if (subCommand.equalsIgnoreCase("clear")) {

                if (game == null) {
                    result = Utils.formatSiegeMessage("There's no game going. Try /game start.");
                    return true;
                }

                if (!game.getState().equalsIgnoreCase("stopped")) {
                    result = Utils.formatSiegeMessage("The game currently is unable to be cleared. Needed game state: stopped, Current game state: " + game.getState());
                    return true;
                }

                holder.setGame(null);
                result = Utils.formatSiegeMessage("Cleared game settings. Use /game start to reconfigure.");
            }
            else {
                result = Utils.formatSiegeMessage("Invalid command " + subCommand + " in /" + command.getName() + " " + subCommand + ".", subCommand);
            }

            return true;
        }
        catch (ClassCastException ex) {
            result = Utils.formatSiegeMessage("You must be a player to send this command.");
        }
        catch (NumberFormatException ex) {
            result = Utils.formatSiegeMessage("Game time must be an integer number.");
        }
        finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return true;
    }
}
