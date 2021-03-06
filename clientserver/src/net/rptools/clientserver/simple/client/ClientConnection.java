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
package net.rptools.clientserver.simple.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.rptools.clientserver.simple.AbstractConnection;


/**
 * @author drice
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ClientConnection extends AbstractConnection {
    private final Socket socket;

    private SendThread send;

    private ReceiveThread receive;

    private final String id;
    
    public ClientConnection(String host, int port, String id) throws UnknownHostException, IOException {
        this(new Socket(host, port), id);
    }

    public ClientConnection(Socket socket, String id) {
        this.socket = socket;
        
        this.id = id;
    }

    public void start() throws IOException {
    	
        sendHandshake(socket);
        
        this.send = new SendThread(this, socket.getOutputStream());
        this.send.start();
        this.receive = new ReceiveThread(this, socket.getInputStream());
        this.receive.start();
    }
    
    public void sendHandshake(Socket s) throws IOException {
    	// No-op
    }
    
    public String getId() {
    	return id;
    }
    public void sendMessage(byte[] message) {
        outQueue.add(message);
        synchronized (send) {
            send.notify();
        }
    }
    
    public boolean isAlive() {
        return send.isAlive() && receive.isAlive();
    }

    public void close() throws IOException {
        socket.close();
        send.requestStop();
        try {
            send.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        receive.requestStop();
        try {
            if (false)
                receive.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // send thread
    // /////////////////////////////////////////////////////////////////////////
    private class SendThread extends Thread {
        private final ClientConnection conn;

        private final OutputStream out;

        private boolean stopRequested = false;

        public SendThread(ClientConnection conn, OutputStream out) {
            this.conn = conn;
            this.out = new BufferedOutputStream(out, 1024);
        }

        public void requestStop() {
            this.stopRequested = true;
            synchronized (this) {
                this.notify();
            }
        }

        public void run() {
            try {
                while (!stopRequested) {

                    try {
                        while (conn.outQueue.size() > 0) {
                            try {
                                byte[] message = conn.outQueue.remove(0);
                                conn.writeMessage(out, message);
                            } catch (IndexOutOfBoundsException e) {
                                // just ignore and wait
                            }
                        }

                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            } catch (IOException e) {
                fireDisconnect();
            }
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // receive thread
    // /////////////////////////////////////////////////////////////////////////
    private class ReceiveThread extends Thread {
        private final ClientConnection conn;

        private final InputStream in;

        private boolean stopRequested = false;

        public ReceiveThread(ClientConnection conn, InputStream in) {
            this.conn = conn;
            this.in = in;
        }

        public void requestStop() {
            stopRequested = true;
        }

        public void run() {
                while (!stopRequested) {

                    try {
                        byte[] message = conn.readMessage(in);
                    	conn.dispatchMessage(conn.id, message);
                    } catch (IOException e) {
                        fireDisconnect();
                        break;
                    }  catch (Throwable t) {
                		// don't let anything kill this thread via exception
                		t.printStackTrace();
                	}
                }

        }
    }

}
