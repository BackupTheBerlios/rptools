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
package net.rptools.clientserver.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rptools.clientserver.ActivityListener;
import net.rptools.clientserver.ActivityListener.Direction;
import net.rptools.clientserver.ActivityListener.State;

/**
 * @author drice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractConnection {
    protected List<byte[]> outQueue = Collections.synchronizedList(new ArrayList<byte[]>());
    protected List<MessageHandler> messageHandlers = Collections.synchronizedList(new ArrayList<MessageHandler>());
    protected List<ActivityListener> listeners = Collections.synchronizedList(new ArrayList<ActivityListener>());
    protected List<DisconnectHandler> disconnectHandlers = Collections.synchronizedList(new ArrayList<DisconnectHandler>());

    public final void addMessageHandler(MessageHandler handler) {
        messageHandlers.add(handler);
    }

    public final void removeMessageHandler(MessageHandler handler) {
        messageHandlers.remove(handler);
    }

    protected final void dispatchMessage(String id, byte[] message) {
        synchronized (messageHandlers) {
            for (MessageHandler handler : messageHandlers) {
                handler.handleMessage(id, message);
            }
        }
    }
    
    public final void fireDisconnect() {
    	
    	for (DisconnectHandler handler : disconnectHandlers) {
    		handler.handleDisconnect(this);
    	}
    }
    
    public final void addActivityListener(ActivityListener listener) {
        listeners.add(listener);
    }
    
    public final void removeActivityListener(ActivityListener listener) {
        listeners.remove(listener);
    }
    
    public final void addDisconnectHandler(DisconnectHandler handler) {
        disconnectHandlers.add(handler);
    }
    
    public final void removeDisconnectHandler(DisconnectHandler handler) {
        disconnectHandlers.remove(handler);
    }
    
    protected final void notifyListeners(Direction direction, State state, int totalTransferSize, int currentTransferSize) {
        synchronized(listeners) {
            for (ActivityListener listener : listeners) {
                listener.notify(direction, state, totalTransferSize, currentTransferSize);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // static helper methods
    ///////////////////////////////////////////////////////////////////////////
    protected final void writeMessage(OutputStream out, byte[] message) throws IOException {
        int length = message.length;
        
        notifyListeners(Direction.Outbound, State.Start, length, 0);

        out.write(length >> 24);
        out.write(length >> 16);
        out.write(length >> 8);
        out.write(length);

        for (int i = 0; i < message.length; i++) {
            out.write(message[i]);
            
            if (i != 0 && i % ActivityListener.CHUNK_SIZE == 0) {
                notifyListeners(Direction.Outbound, State.Progress, length, i);
            }
        }
        
        out.flush();
        
        notifyListeners(Direction.Outbound, State.Complete, length, length);
    }

    protected final byte[] readMessage(InputStream in) throws IOException {
        int b32 = in.read();
        int b24 = in.read();
        int b16 = in.read();
        int b8 = in.read();

        int length = (b32 << 24) + (b24 << 16) + (b16 << 8) + b8;
        
        notifyListeners(Direction.Inbound, State.Start, length, 0);

        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = (byte) in.read();
            
            if (i != 0 && i % ActivityListener.CHUNK_SIZE == 0) {
                notifyListeners(Direction.Inbound, State.Progress, length, i);
            }
        }

        notifyListeners(Direction.Inbound, State.Complete, length, length);

        return ret;
    }
}
