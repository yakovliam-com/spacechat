package dev.spaceseries.spacechat.messaging.redis.supervisor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.messaging.MessengerSupervisor;
import dev.spaceseries.spacechat.messaging.packet.receive.ReceiveMessageDataPacket;
import dev.spaceseries.spacechat.messaging.packet.send.SendMessageDataPacket;
import dev.spaceseries.spacechat.messaging.redis.packet.RedisPublishDataPacket;
import dev.spaceseries.spacechat.messaging.redis.packet.RedisStringReceiveDataPacket;
import dev.spaceseries.spacechat.messaging.redis.packet.broadcast.RedisBroadcastPacket;
import dev.spaceseries.spacechat.messaging.redis.packet.broadcast.RedisBroadcastPacketDeserializer;
import dev.spaceseries.spacechat.messaging.redis.packet.broadcast.RedisBroadcastPacketSerializer;
import dev.spaceseries.spacechat.messaging.redis.packet.chat.RedisChatPacket;
import dev.spaceseries.spacechat.messaging.redis.packet.chat.RedisChatPacketDeserializer;
import dev.spaceseries.spacechat.messaging.redis.packet.chat.RedisChatPacketSerializer;
import dev.spaceseries.spacechat.messaging.redis.connector.RedisConnector;
import dev.spaceseries.spacechat.util.chat.ChatUtil;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class RedisSupervisor extends MessengerSupervisor {

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
    @Override
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
     * Publish a message
     *
     * @param data data
     */
    @Override
    public void publishChatMessage(SendMessageDataPacket<?> data) {
        RedisChatPacket redisChatPacket = (RedisChatPacket) data;

        // gson-ify the redis message
        String json = gson.toJson(redisChatPacket, RedisChatPacket.class);

        // publish to redis
        this.getSupervised().publish(new RedisPublishDataPacket(REDIS_CHAT_CHANNEL.get(Config.get()), json));
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
     * Publish a broadcast message
     *
     * @param data data
     */
    @Override
    public void publishBroadcast(SendMessageDataPacket<?> data) {
        RedisBroadcastPacket packet = (RedisBroadcastPacket) data;

        // gson-ify the redis message
        String json = gson.toJson(packet, RedisBroadcastPacket.class);

        // publish to redis
        this.getSupervised().publish(new RedisPublishDataPacket(REDIS_BROADCAST_CHANNEL.get(Config.get()), json));
    }

    /**
     * Receive incoming chat message
     *
     * @param data data
     */
    @Override
    public void receiveChatMessage(ReceiveMessageDataPacket<?> data) {
        RedisStringReceiveDataPacket packet = (RedisStringReceiveDataPacket) data;
        // deserialize
        RedisChatPacket chatPacket = gson.fromJson(packet.getData(), RedisChatPacket.class);

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
     * @param data data
     */
    @Override
    public void receiveBroadcast(ReceiveMessageDataPacket<?> data) {
        RedisStringReceiveDataPacket packet = (RedisStringReceiveDataPacket) data;

        // deserialize
        RedisBroadcastPacket broadcastPacket = gson.fromJson(packet.getData(), RedisBroadcastPacket.class);

        // if the message is from ourselves, then return
        if (broadcastPacket.getServerIdentifier().equalsIgnoreCase(REDIS_SERVER_IDENTIFIER.get(Config.get()))) {
            return;
        }

        // send to all players
        ChatUtil.sendComponentMessage(broadcastPacket.getComponent());
    }
}
