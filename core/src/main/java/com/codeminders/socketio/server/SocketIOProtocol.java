package com.codeminders.socketio.server;

import com.codeminders.socketio.util.JSON;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of Socket.IO Protocol version 4
 *
 * @author Alexander Sova (bird@codeminders.com)
 */
public final class SocketIOProtocol
{
    private SocketIOProtocol()
    {
    }

    public static String encode(SocketIOPacket packet)
    {
        return String.valueOf(packet.getType().value()) + packet.getData();
    }

    public static SocketIOPacket decode(String raw)
            throws SocketIOProtocolException
    {
        assert (raw != null);

        if(raw.length() < 1)
            throw new SocketIOProtocolException("Empty SocketIO packet");

        try
        {
            return new SocketIOPacket(
                    SocketIOPacket.Type.fromInt(Integer.parseUnsignedInt(raw.substring(0, 1))),
                    raw.substring(1));
        }
        catch (NumberFormatException e)
        {
            throw new SocketIOProtocolException("Invalid EIO packet type: " + raw);
        }
    }

    public static SocketIOPacket createEventPacket(String name, Object... args)
    {
        ArrayList<Object> data = new ArrayList<>();
        data.add(name);
        data.addAll(Arrays.asList(args));

        return new SocketIOPacket(SocketIOPacket.Type.EVENT, JSON.toString(data.toArray()));
    }

    public static SocketIOPacket createDisconnectPacket()
    {
        return new SocketIOPacket(SocketIOPacket.Type.DISCONNECT);
    }
}
