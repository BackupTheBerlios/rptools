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

/**
 * @author drice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractConnection {

    protected List<byte[]> outQueue = Collections.synchronizedList(new ArrayList<byte[]>());

    protected List<MessageHandler> handlers = Collections.synchronizedList(new ArrayList<MessageHandler>());

    public final void addMessageHandler(MessageHandler handler) {
        handlers.add(handler);
    }

    public final void removeMessageHandler(MessageHandler handler) {
        handlers.remove(handler);
    }

    protected final void dispatchMessage(String id, byte[] message) {
        synchronized (handlers) {
            for (MessageHandler handler : handlers) {
                handler.handleMessage(id, message);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // static helper methods
    ///////////////////////////////////////////////////////////////////////////
    protected static final void writeMessage(OutputStream out, byte[] message) throws IOException {
        int length = message.length;

        out.write(length >> 24);
        out.write(length >> 16);
        out.write(length >> 8);
        out.write(length);

        out.write(message);
    }

    protected static final byte[] readMessage(InputStream in) throws IOException {
        int b32 = in.read();
        int b24 = in.read();
        int b16 = in.read();
        int b8 = in.read();

        int length = (b32 << 24) + (b24 << 16) + (b16 << 8) + b8;

        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = (byte) in.read();
        }

        return ret;
    }
}
