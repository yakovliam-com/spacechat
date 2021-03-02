package dev.spaceseries.spacechat.dc.redis.supervisor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.dc.Supervisor;
import dev.spaceseries.spacechat.dc.redis.RedisChatMessage;
import dev.spaceseries.spacechat.dc.redis.RedisChatMessageDeserializer;
import dev.spaceseries.spacechat.dc.redis.RedisChatMessageSerializer;
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
                .registerTypeAdapter(RedisChatMessage.class, new RedisChatMessageSerializer())
                .registerTypeAdapter(RedisChatMessage.class, new RedisChatMessageDeserializer())
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
     * @param redisChatMessage redis chat message
     */
    public void publishChatMessage(RedisChatMessage redisChatMessage) {
        // gson-ify the redis message
        String json = gson.toJson(redisChatMessage, RedisChatMessage.class);

        // publish to redis
        this.getSupervised().publish(REDIS_CHAT_CHANNEL.get(Config.get()), json);
    }

    /**
     * Receive incoming chat message
     *
     * @param raw raw string
     */
    public void receiveChatMessage(String raw) {
        // deserialize
        RedisChatMessage chatMessage = gson.fromJson(raw, RedisChatMessage.class);

        // if the message is from ourselves, then return
        if (chatMessage.getServerIdentifier().equalsIgnoreCase(REDIS_SERVER_IDENTIFIER.get(Config.get()))) {
            return;
        }

        // send to all players
        ChatUtil.sendComponentChatMessage(chatMessage.getComponent());
    }
}
