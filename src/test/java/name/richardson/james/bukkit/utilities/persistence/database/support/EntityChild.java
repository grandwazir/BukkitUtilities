/*******************************************************************************
 Copyright (c) 2013 James Richardson.

 EntityChild.java is part of bukkit-utilities.

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

package name.richardson.james.bukkit.utilities.persistence.database.support;

import javax.persistence.*;
import java.util.List;

import name.richardson.james.bukkit.utilities.persistence.database.AbstractRecord;

@Entity
public class EntityChild extends AbstractRecord {

	@ManyToOne(targetEntity = EntityParent.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	@PrimaryKeyJoinColumn(name = "parentId", referencedColumnName = "id")
	private List<EntityChild> children;

}
