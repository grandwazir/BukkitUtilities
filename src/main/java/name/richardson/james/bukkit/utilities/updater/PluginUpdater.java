/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 *
 * PluginUpdater.java is part of BukkitUtilities.
 *
 * BukkitUtilities is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * BukkitUtilities is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * BukkitUtilities. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package name.richardson.james.bukkit.utilities.updater;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Listener;

import name.richardson.james.bukkit.utilities.formatters.ColourFormatter;
import name.richardson.james.bukkit.utilities.localisation.Localised;
import name.richardson.james.bukkit.utilities.localisation.ResourceBundles;
import name.richardson.james.bukkit.utilities.logging.PluginLogger;

public class PluginUpdater implements Listener, Runnable, Localised {

	private static final ResourceBundle localisation = ResourceBundle.getBundle(ResourceBundles.MESSAGES.getBundleName());

	public enum Branch {
		DEVELOPMENT, STABLE
	}

	public enum State {
		NOTIFY, OFF
	}

	private final String artifactId;
	private final String groupId;
	private final Logger logger = PluginLogger.getLogger(PluginUpdater.class);
	private final String pluginName;
	private final URL repositoryURL;
	private final String version;
	/* A reference to the downloaded Maven manifest from the remote repository */
	private MavenManifest manifest;

	public PluginUpdater(final Updatable plugin) {
		this.version = plugin.getVersion();
		this.artifactId = plugin.getArtifactID();
		this.groupId = plugin.getGroupID();
		this.repositoryURL = plugin.getRepositoryURL();
		this.pluginName = plugin.getName();
	}

	public String getMessage(final String key) {
		String message = PluginUpdater.localisation.getString(key);
		message = ColourFormatter.replace(message);
		return message;
	}

	public String getMessage(final String key, final Object... elements) {
		final MessageFormat formatter = new MessageFormat(PluginUpdater.localisation.getString(key));
		formatter.setLocale(Locale.getDefault());
		String message = formatter.format(elements);
		message = ColourFormatter.replace(message);
		return message;
	}

	public void run() {
		try {
			this.parseMavenMetaData();
			if (this.isNewVersionAvailable()) {
				String newVersionNotification = this.getMessage("notice.updater.new-version-available", this.pluginName, this.manifest.getCurrentVersion());
				this.logger.log(Level.INFO, newVersionNotification);
			}
		} catch (final IOException e) {
			this.logger.log(Level.WARNING, "warning.updater.unable-to-read-metadata", this.repositoryURL.toString());
		} catch (final SAXException e) {
			this.logger.log(Level.WARNING, "warning.updater.unable-to-read-metadata", this.repositoryURL.toString());
		} catch (final ParserConfigurationException e) {
			this.logger.log(Level.WARNING, "warning.updater.unable-to-read-metadata", this.repositoryURL.toString());
		}
	}

	private void getMavenMetaData(final File storage)
	throws IOException {
		final StringBuilder path = new StringBuilder();
		path.append(this.repositoryURL);
		path.append("/");
		path.append(this.groupId.replace(".", "/"));
		path.append("/");
		path.append(this.artifactId);
		path.append("/maven-metadata.xml");
		final URL url = new URL(path.toString());
		// get the file
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		try {
			this.logger.log(Level.FINER, "Getting manifest: {0}", url.toString());
			rbc = Channels.newChannel(url.openStream());
			fos = new FileOutputStream(storage);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		} finally {
			if (rbc != null) {
				rbc.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	private boolean isNewVersionAvailable() {
		final DefaultArtifactVersion current = new DefaultArtifactVersion(this.version);
		final DefaultArtifactVersion target = new DefaultArtifactVersion(this.manifest.getCurrentVersion());
		final Object params[] = {target.toString(), current.toString()};
		if (current.compareTo(target) == -1) {
			this.logger.log(Level.FINE, "New version available: {0} > {1}", params);
			return true;
		} else {
			this.logger.log(Level.FINE, "New version unavailable: {0} <= {1}", params);
			return false;
		}
	}

	private void parseMavenMetaData()
	throws IOException, SAXException, ParserConfigurationException {
		final File temp = File.createTempFile(this.artifactId, null);
		this.logger.log(Level.FINER, "Creating temporary manifest: {0}", temp.getAbsolutePath());
		this.getMavenMetaData(temp);
		this.manifest = new MavenManifest(temp);
	}

}
