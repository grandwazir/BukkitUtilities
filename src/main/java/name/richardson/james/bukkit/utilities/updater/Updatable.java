/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * Updatable.java is part of BukkitUtilities.
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

import java.net.URL;

/**
 * The Interface Updatable.
 */
public interface Updatable {

  /**
   * Gets the artifact id of the Maven project.
   * 
   * @return the artifact id
   */
  public String getArtifactID();

  /**
   * Gets the group id of the Maven artifact.
   * 
   * @return the group id
   */
  public String getGroupID();

  /**
   * Gets the url of the repository that contains this plugin.
   * 
   * @return the repository url
   */
  public URL getRepositoryURL();

}
