/*******************************************************************************
 Copyright (c) 2013 James Richardson.

 OnlinePlayerMatcherTest.java is part of bukkit-utilities.

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

package name.richardson.james.bukkit.utilities.matchers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(value = BlockJUnit4ClassRunner.class)
public class OnlinePlayerMatcherTest extends TestCase {

	private static final String[] PLAYER_NAMES = {"grandwazir", "Sergeant_Subtle"};

	private Server server;
	private List<Player> players;

	@Before
	public void setUp()
	throws Exception {
		this.server = EasyMock.createNiceMock(Server.class);
		this.players = new ArrayList<Player>();
		for (String name : PLAYER_NAMES) {
			Player player = EasyMock.createNiceMock(Player.class);
			EasyMock.expect(player.getName()).andReturn(name).times(2);
			EasyMock.replay(player);
			players.add(player);
		}
		EasyMock.expect(server.getOnlinePlayers()).andStubReturn(players.toArray(new Player[players.size()]));
		EasyMock.replay(this.server);
	}

	@Test
	public void testPartialMatch()
	throws Exception {
		OnlinePlayerMatcher.setServer(server);
		OnlinePlayerMatcher matcher = new OnlinePlayerMatcher();
		List<String> matches = matcher.getMatches("gran");
		Assert.assertTrue("List does not contain 'grandwazir'", matches.contains("grandwazir"));
		Assert.assertTrue("List is not equal to 1", matches.size() == 1);
	}

	@Test
	public void testExactMatch()
	throws Exception {
		OnlinePlayerMatcher.setServer(server);
		OnlinePlayerMatcher matcher = new OnlinePlayerMatcher();
		List<String> matches = matcher.getMatches("grandwazir");
		Assert.assertTrue("List does not contain 'grandwazir'", matches.contains("grandwazir"));
		Assert.assertTrue("List is not equal to 1", matches.size() == 1);
	}

	@Test
	public void testBlankMatch()
	throws Exception {
		OnlinePlayerMatcher.setServer(server);
		OnlinePlayerMatcher matcher = new OnlinePlayerMatcher();
		List<String> matches = matcher.getMatches("");
		Assert.assertTrue("List does not contain 'grandwazir'", matches.contains("grandwazir"));
		Assert.assertTrue("List does not contain 'Sergeant_Subtle'", matches.contains("Sergeant_Subtle"));
		Assert.assertTrue("List is not equal to 2", matches.size() == 2);
	}

	@Test
	public void testCaseInsensitiveMatch()
	throws Exception {
		OnlinePlayerMatcher.setServer(server);
		OnlinePlayerMatcher matcher = new OnlinePlayerMatcher();
		List<String> matches = matcher.getMatches("SERGEANT_SUBTL");
		Assert.assertTrue("List does not contain 'Sergeant_Subtle'", matches.contains("Sergeant_Subtle"));
		Assert.assertTrue(matches.size() == 1);
	}

	@Test(expected = IllegalStateException.class)
	public void testServerMustBeSet()
	throws Exception {
		OnlinePlayerMatcher.setServer(server);
		OnlinePlayerMatcher matcher = new OnlinePlayerMatcher();
		OnlinePlayerMatcher.setServer(null);
		matcher.getMatches("");
	}

	@Test(expected = IllegalStateException.class)
	public void testServerMustBeSetBeforeConstruction()
	throws Exception {
		WorldMatcher.setServer(null);
		new OnlinePlayerMatcher();
	}

}
