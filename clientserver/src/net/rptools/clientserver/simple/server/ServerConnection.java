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
package net.rptools.clientserver.simple.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rptools.clientserver.simple.AbstractConnection;
import net.rptools.clientserver.simple.DisconnectHandler;
import net.rptools.clientserver.simple.MessageHandler;
import net.rptools.clientserver.simple.client.ClientConnection;


/**
 * @author drice
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ServerConnection extends AbstractConnection implements MessageHandler, DisconnectHandler {
    private final ServerSocket socket;

    private final ListeningThread listeningThread;

    private final DispatchThread dispatchThread;

    private Map<String, ClientConnection> clients = Collections
            .synchronizedMap(new HashMap<String, ClientConnection>());
    
    private List<ServerObserver> observerList = Collections.synchronizedList(new ArrayList<ServerObserver>());

    public ServerConnection(int port) throws IOException {
        socket = new ServerSocket(port);
        dispatchThread = new DispatchThread(this);
        dispatchThread.start();
        listeningThread = new ListeningThread(this, socket);
        listeningThread.start();
    }

    public void addObserver(ServerObserver observer) {
    	observerList.add(observer);
    }
    
    public void removeObserver(ServerObserver observer) {
    	observerList.remove(observer);
    }
    
    public void handleMessage(String id, byte[] message) {
        dispatchMessage(id, message);
    }

    public void broadcastMessage(byte[] message) {
        synchronized (clients) {
            for (ClientConnection conn : clients.values()) {
                conn.sendMessage(message);
            }
        }
    }

    public void broadcastMessage(String[] exclude, byte[] message) {
        Set<String> excludeSet = new HashSet<String>();
        for (String e : exclude) {
            excludeSet.add(e);
        }
        synchronized (clients) {
            for (Map.Entry<String, ClientConnection> entry : clients.entrySet()) {
                if (!excludeSet.contains(entry.getKey())) {
                    entry.getValue().sendMessage(message);
                }
            }
        }
    }

    public void sendMessage(String id, byte[] message) {
        ClientConnection client = clients.get(id);
        client.sendMessage(message);
    }

    /**
     * Server subclasses may override this method to perform serial handshaking
     * before the connection is accepted into its pool.  By default, this just
     * returns true.
     * @param conn
     * @return true if the connection should be added to the pool
     */
    public boolean handleConnectionHandshake(String id, Socket socket) {
    	return true;
    }
    
    public void close() throws IOException {
        socket.close();

        synchronized (clients) {
            for (ClientConnection conn : clients.values()) {
                conn.close();
            }
        }

        listeningThread.requestStop();
        try {
            listeningThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void reapClients() {
        synchronized (clients) {
            
            for (Iterator<Map.Entry<String, ClientConnection>> i = clients.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry<String, ClientConnection> entry = i.next();
                String key = entry.getKey();
                ClientConnection conn = entry.getValue();
                if (!conn.isAlive()) {
                    try {
                        i.remove();
                        fireClientDisconnect(conn);
                        conn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void fireClientConnect(ClientConnection conn) {
    	
    	for (ServerObserver observer : observerList) {
    		observer.connectionAdded(conn);
    	}
    }
    
    private void fireClientDisconnect(ClientConnection conn) {
    	
    	for (ServerObserver observer : observerList) {
    		observer.connectionRemoved(conn);
    	}
    }

    ////
    // DISCONNECT HANDLER
    public void handleDisconnect(AbstractConnection conn) {
    	if (conn instanceof ClientConnection) {
    		fireClientDisconnect((ClientConnection) conn);
    	}
    }
    
    ////
    // Threads
    private static class ListeningThread extends Thread {
        private final ServerConnection server;

        private final ServerSocket socket;

        private boolean stopRequested = false;

        private int nextConnectionId = 0;
        
        private synchronized String nextClientId(Socket socket) {
        	return socket.getInetAddress().getHostAddress() + "-" + (nextConnectionId++);
        }
        
        public ListeningThread(ServerConnection server, ServerSocket socket) {
            this.server = server;
            this.socket = socket;
        }

        public void requestStop() {
            stopRequested = true;
        }

        public void run() {
            while (!stopRequested) {
                try {
                    Socket s = socket.accept();

                    String id = nextClientId(s);
                    
                    // Make sure the client is allowed
                    if (!server.handleConnectionHandshake(id, s)) {
                    	s.close();
                    	continue;
                    }
                    
                    ClientConnection conn = new ClientConnection(s, id);
                    conn.addMessageHandler(server);
                    conn.addDisconnectHandler(server);
                    conn.start();

                    synchronized (server.clients) {
                        server.reapClients();
                        
                        server.clients.put(conn.getId(), conn);
                        server.fireClientConnect(conn);
                        //System.out.println("new client " + conn.getId() + " added, " + server.clients.size() + " total");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    
    private static class DispatchThread extends Thread implements MessageHandler {
        private final ServerConnection server;

        private List<Message> queue = Collections.synchronizedList(new ArrayList<Message>());

        private boolean stopRequested = false;

        public DispatchThread(ServerConnection server) {
            this.server = server;
        }

        public void requestStop() {
            stopRequested = true;
        }

        public void handleMessage(String id, byte[] message) {
            queue.add(new Message(id, message));
            synchronized (this) {
                this.notify();
            }
        }

        public void run() {
            while (!stopRequested) {
                while (queue.size() > 0) {
                    Message msg = queue.remove(0);

                    server.handleMessage(msg.id, msg.message);
                }

                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private static class Message {
        final String id;

        final byte[] message;

        public Message(String id, byte[] message) {
            this.id = id;
            this.message = message;
        }
    }
}
