/*******************************************************************************
 Copyright (c) 2013 James Richardson.

 PlayerNotifier.java is part of BukkitUtilities.

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
package name.richardson.james.bukkit.utilities.updater;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import name.richardson.james.bukkit.utilities.listener.AbstractListener;
import name.richardson.james.bukkit.utilities.localisation.*;

import static name.richardson.james.bukkit.utilities.localisation.BukkitUtilities.UPDATER_NEW_VERSION_AVAILABLE;

/**
 * The PlayerNotifier is responsible for notifying players which a specific permission that there is an update available for the plugin. The players will be
 * notified when they join the server. The permission required for players to receive the notice is the name of the plugin in lowercase.
 */
public class PlayerNotifier extends AbstractListener {

	private final String permission;
	private final String pluginName;
	private final PluginUpdater updater;

	public PlayerNotifier(Plugin plugin, PluginManager pluginManager, PluginUpdater updater) {
		super(plugin, pluginManager);
		this.pluginName = plugin.getName();
		this.permission = pluginName.toLowerCase();
		this.updater = updater;
	}

	/**
	 * Notify the player logging of the available update if they have the required permission.
	 *
	 * @param event
	 */
	@SuppressWarnings("PublicMethodNotExposedInInterface")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final boolean notify = event.getPlayer().hasPermission(this.permission);
		if (notify && updater.isNewVersionAvailable()) {
			event.getPlayer().sendMessage(UPDATER_NEW_VERSION_AVAILABLE.asInfoMessage(pluginName, updater.getLatestRemoteVersion().getMajorVersion(), updater.getLatestRemoteVersion().getMinorVersion()));
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PlayerNotifier{");
		sb.append(", permission='").append(permission).append('\'');
		sb.append(", pluginName='").append(pluginName).append('\'');
		sb.append(", updater=").append(updater);
		sb.append('}');
		return sb.toString();
	}
}