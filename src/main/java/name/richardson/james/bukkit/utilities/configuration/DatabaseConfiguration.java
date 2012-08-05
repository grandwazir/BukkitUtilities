package name.richardson.james.bukkit.utilities.configuration;

import java.io.IOException;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import name.richardson.james.bukkit.utilities.persistence.AbstractYAMLStorage;
import name.richardson.james.bukkit.utilities.plugin.Plugin;

public final class DatabaseConfiguration extends AbstractYAMLStorage {

  public static final String FILE_NAME = "database.yml";
  
  private ServerConfig serverConfig;
  
  private DataSourceConfig dataSourceConfig;
  
  public DatabaseConfiguration(Plugin plugin) throws IOException {
    super(plugin, FILE_NAME);
  }
  
  public ServerConfig getServerConfig() {
    if (this.serverConfig == null) {
      this.serverConfig = new ServerConfig();
      this.serverConfig.setDefaultServer(false);
      this.serverConfig.setRegister(false);
      // configure database defaults from bukkit.yml
      Bukkit.getServer().configureDbConfig(serverConfig);
    }
    return this.serverConfig;
  }
  
  public DataSourceConfig getDataSourceConfig() {
    if (this.dataSourceConfig == null) {
      ConfigurationSection section = this.getConfiguration().getConfigurationSection("database");
      this.dataSourceConfig = this.serverConfig.getDataSourceConfig();
      if (section.get("username") != null) this.dataSourceConfig.setUsername(section.getString("username"));
      if (section.get("password") != null) this.dataSourceConfig.setPassword((section.getString("password")));
      if (section.get("url") != null) this.dataSourceConfig.setUrl((section.getString("url")));
      if (section.get("driver") != null) this.dataSourceConfig.setDriver((section.getString("driver")));
      if (section.get("isolation") != null) this.dataSourceConfig.setIsolationLevel(TransactionIsolation.getLevel(section.getString("isolation")));
    }
    return this.dataSourceConfig;
  }

}