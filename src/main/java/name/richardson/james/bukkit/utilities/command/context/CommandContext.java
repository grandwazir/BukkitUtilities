/*******************************************************************************
 Copyright (c) 2013 James Richardson.

 CommandContext.java is part of bukkit-utilities.

 BukkitUtilities is free software: you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by the Free
 Software Foundation, either version 3 of the License, or (at your option) any
 later version.

 BukkitUtilities is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with
 BukkitUtilities. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package name.richardson.james.bukkit.utilities.command.context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A object that represents the context that a {@link Command} has been executed within. This object is responsible for parsing the arguments and providing
 * convenience methods to retrieve them. It is up to individual implementations to decide what to do when a requested argument does not exist.
 */
public interface CommandContext {

	/**
	 * Get the CommandSender who called this command.
	 *
	 * @return the CommandSender
	 */
	CommandSender getCommandSender();

	/**
	 * Get the contents of the flag.
	 *
	 * @param label the flag label to look up.
	 * @return the contents of the flag.
	 */
	String getFlag(String label);

	/**
	 * Join all the arguments from a specified index onwards into one String.
	 *
	 * @param initialIndex the index to start at at
	 * @return a String containing all the arguments seperated by ' '.
	 */
	String getJoinedArguments(int initialIndex);

	/**
	 * Convert the argument in the specified index to a {@link OfflinePlayer}.
	 *
	 * @param index the argument number to use.
	 * @return a matching OfflinePlayer using the argument index as the player's name.
	 */
	OfflinePlayer getOfflinePlayer(int index);

	/**
	 * Convert the argument in the specified index to an integer.
	 *
	 * @param index
	 * @return
	 */
	int getInt(int index);

	/**
	 * Convert the argument in the specified index to a {@link Player}.
	 *
	 * @param index the argument number to use.
	 * @return a matching Player using the argument index as the player's name..
	 */
	Player getPlayer(int index);

	/**
	 * Get the argument at the specified index.
	 *
	 * @param index the argument number to fetch.
	 * @return the argument specified.
	 */
	String getString(int index);

	/**
	 * Check to see if the context contains an argument.
	 *
	 * @param index the argument number to check
	 * @return true if the argument exists, false otherwise.
	 */
	boolean has(int index);

	/**
	 * Check to see if the context contains a flag.
	 *
	 * @param label the prefix of the flag to check
	 * @return true if the flag exists, false otherwise.
	 */
	boolean hasFlag(String label);

	/**
	 * Check if the {@link CommandSender} is not an instance of {@link Player}.
	 *
	 * @return false if the CommandSender is a Player, otherwise true.
	 */
	boolean isConsoleCommandSender();

	/**
	 * Get the total number of arguments contained within this context. The total does not include the CommandSender or any optional flags.
	 *
	 * @return total number of arguments.
	 */
	int size();

}
