package dev.spaceseries.spacechat.dynamicconnection;

public abstract class Supervisor<T> {

    /**
     * Supervised
     */
    protected T supervised;

    /**
     * Construct supervisor
     *
     * @param supervised supervised
     */
    public Supervisor(T supervised) {
        this.supervised = supervised;
    }

    /**
     * Construct supervisor
     *
     */
    public Supervisor() {
    }

    /**
     * Returns supervised
     *
     * @return supervised
     */
    public T getSupervised() {
        return supervised;
    }

    /**
     * Stops a supervised task from running
     */
    public abstract void stop();
}
