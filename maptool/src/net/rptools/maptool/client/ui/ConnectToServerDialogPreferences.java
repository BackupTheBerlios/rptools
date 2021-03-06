package net.rptools.maptool.client.ui;

import java.util.prefs.Preferences;

import net.rptools.maptool.client.AppConstants;
import net.rptools.maptool.model.Player;
import net.rptools.maptool.server.MapToolServer;

public class ConnectToServerDialogPreferences {

    private static Preferences prefs = Preferences.userRoot().node(AppConstants.APP_NAME + "/prefs/connect");        

    private static final String KEY_USERNAME = "name";
    private static final String KEY_ROLE = "role";
    private static final String KEY_HOST = "host";
    private static final String KEY_PORT = "port";
    
    public String getUsername() {
    	return prefs.get(KEY_USERNAME, "");
    }
    
    public void setUsername(String name) {
    	prefs.put(KEY_USERNAME, name);
    }
    
    public int getRole () {
    	return prefs.getInt(KEY_ROLE, Player.Role.PLAYER);
    }
    
    public void setRole(int role) {
    	prefs.putInt(KEY_ROLE, role);
    }

    public void setHost(String host) {
    	prefs.put(KEY_HOST, host);
    }
    
    public String getHost() {
    	return prefs.get(KEY_HOST, "");
    }
    
    public int getPort() {
    	return prefs.getInt(KEY_PORT, MapToolServer.DEFAULT_PORT);
    }
    
    public void setPort(int port) {
    	prefs.putInt(KEY_PORT, port);
    }
    
}
