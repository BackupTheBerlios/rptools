/*
 * Created on Jan 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.rptools.clientserver.hessian.server;

import java.io.IOException;

import net.rptools.clientserver.hessian.HessianUtils;


/**
 * @author drice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServerConnection extends net.rptools.clientserver.simple.server.ServerConnection {
    public ServerConnection(int port) throws IOException {
        super(port);
    }
    
    public void broadcastCallMethod(String method, Object... parameters) {
        broadcastMessage(HessianUtils.methodToBytes(method, parameters));
    }
    
    public void broadcastCallMethod(String[] exclude, String method, Object... parameters) {
        broadcastMessage(exclude, HessianUtils.methodToBytes(method, parameters));
    }
    
    public void callMethod(String id, String method, Object... parameters) {
        sendMessage(id, HessianUtils.methodToBytes(method, parameters));
    }
}
