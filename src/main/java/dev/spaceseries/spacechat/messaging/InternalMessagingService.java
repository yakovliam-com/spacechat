package dev.spaceseries.spacechat.messaging;

public abstract class InternalMessagingService<T extends MessengerSupervisor> {

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
