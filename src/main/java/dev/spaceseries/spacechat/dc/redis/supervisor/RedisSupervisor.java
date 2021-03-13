package dev.spaceseries.spacechat.dc.redis.supervisor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dc.Supervisor;
import dev.spaceseries.spacechat.dc.redis.packet.broadcast.RedisBroadcastPacket;
import dev.spaceseries.spacechat.dc.redis.packet.broadcast.RedisBroadcastPacketDeserializer;
import dev.spaceseries.spacechat.dc.redis.packet.broadcast.RedisBroadcastPacketSerializer;
import dev.spaceseries.spacechat.dc.redis.packet.chat.RedisChatPacket;
import dev.spaceseries.spacechat.dc.redis.packet.chat.RedisChatPacketDeserializer;
import dev.spaceseries.spacechat.dc.redis.packet.chat.RedisChatPacketSerializer;
import dev.spaceseries.spacechat.dc.redis.connector.RedisConnector;
import dev.spaceseries.spacechat.util.chat.ChatUtil;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class RedisSupervisor extends Supervisor<RedisConnector> {

    /**
     * Gson
     */
    private Gson gson;

    /**
     * Construct supervisor
     */
    public RedisSupervisor() {
    }

    /**
     * Initialize
     */
    public void initialize() {
        // set supervised
        this.supervised = new RedisConnector(this);

        // initialize my super-duper gson adapter
        gson = new GsonBuilder()
                .registerTypeAdapter(RedisChatPacket.class, new RedisChatPacketSerializer())
                .registerTypeAdapter(RedisChatPacket.class, new RedisChatPacketDeserializer())
                .registerTypeAdapter(RedisBroadcastPacket.class, new RedisBroadcastPacketSerializer())
                .registerTypeAdapter(RedisBroadcastPacket.class, new RedisBroadcastPacketDeserializer())
                .create();
    }

    /**
     * Stops connections of a supervised task
     */
    @Override
    public void stop() {
        if (this.getSupervised() != null) {
            // shutdown
            this.getSupervised().shutdown();
        }
    }

    /**
     * Publish a message
     *
     * @param redisChatPacket redis chat message
     */
    public void publishChatMessage(RedisChatPacket redisChatPacket) {
        // gson-ify the redis message
        String json = gson.toJson(redisChatPacket, RedisChatPacket.class);

        // publish to redis
        this.getSupervised().publish(REDIS_CHAT_CHANNEL.get(Config.get()), json);
    }

    /**
     * Publish a broadcast message
     *
     * @param redisBroadcastPacket redis broadcast packet
     */
    public void publishBroadcast(RedisBroadcastPacket redisBroadcastPacket) {
        // gson-ify the redis message
        String json = gson.toJson(redisBroadcastPacket, RedisBroadcastPacket.class);

        // publish to redis
        this.getSupervised().publish(REDIS_BROADCAST_CHANNEL.get(Config.get()), json);
    }

    /**
     * Receive incoming chat message
     *
     * @param raw raw string
     */
    public void receiveChatMessage(String raw) {
        // deserialize
        RedisChatPacket chatPacket = gson.fromJson(raw, RedisChatPacket.class);

        // if the message is from ourselves, then return
        if (chatPacket.getServerIdentifier().equalsIgnoreCase(REDIS_SERVER_IDENTIFIER.get(Config.get()))) {
            return;
        }

        // send to all players
        ChatUtil.sendComponentChatMessage(chatPacket.getComponent());
    }

    /**
     * Receive incoming broadcast
     *
     * @param raw raw string
     */
    public void receiveBroadcast(String raw) {
        // deserialize
        RedisBroadcastPacket broadcastPacket = gson.fromJson(raw, RedisBroadcastPacket.class);

        // if the message is from ourselves, then return
        if (broadcastPacket.getServerIdentifier().equalsIgnoreCase(REDIS_SERVER_IDENTIFIER.get(Config.get()))) {
            return;
        }

        // send to all players
        ChatUtil.sendComponentChatMessage(broadcastPacket.getComponent());
    }
}
