package dev.spaceseries.spacechat.factory;

/**
 * @param <S> The parameter passed into the factory construction method
 * @param <U> The returned object
 */
public interface IFactory<S, U> {

    /**
     * Constructs a U given an S
     *
     * @param s The S
     * @return The U
     */
    U construct(S s);
}
