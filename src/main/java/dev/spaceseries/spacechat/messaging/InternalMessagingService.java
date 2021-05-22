package dev.spaceseries.spacechat.messaging;

public abstract class InternalMessagingService<T extends MessageHandlerSupervisor> {

    /**
     * Supervisor
     */
    T supervisor;

    /**
     * Returns supervisor
     *
     * @return supervisor
     */
    public T getSupervisor() {
        return supervisor;
    }
}
