/* The MIT License
 * 
 * Copyright (c) 2005 David Rice, Trevor Croft
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, 
 * publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package net.rptools.clientserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.rptools.clientserver.hessian.AbstractMethodHandler;
import net.rptools.clientserver.hessian.client.ClientConnection;
import net.rptools.clientserver.hessian.server.ServerConnection;


import com.caucho.hessian.io.HessianOutput;

/**
 * @author drice
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Test01 {

    public static void main(String[] args) throws Exception {
        ServerConnection server = new ServerConnection(4444);
        server.addMessageHandler(new ServerHandler());

        ClientConnection client = new ClientConnection("192.168.1.102", 4444);
        client.addMessageHandler(new ClientHandler());

        for (int i = 0; i < 1000; i++) {
            if (i % 3 == 0) {
                client.callMethod("fromClient", "arg1", "arg2");
            }
            server.broadcastCallMethod("fromServer", "arg1");
            Thread.sleep(1000);
        }

        client.close();
        server.close();

    }

    private static byte[] getOutput(String method) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        HessianOutput hout = new HessianOutput(bout);

        hout.call(method, new Object[0]);

        return bout.toByteArray();
    }

    private static class ServerHandler extends AbstractMethodHandler {

        public void handleMethod(String id, String method, Object... parameters) {
            System.out.println("Server received: " + method + " from " + id + " args=" + parameters.length);
            for (Object param : parameters) {
                System.out.println("\t" + param);
            }
        }
    }

    private static class ClientHandler extends AbstractMethodHandler {
        public void handleMethod(String id, String method, Object... parameters) {
            System.out.println("Client received: " + method + " from " + id + " args=" + parameters.length);
            for (Object param : parameters) {
                System.out.println("\t" + param);
            }
        }
    }
}
