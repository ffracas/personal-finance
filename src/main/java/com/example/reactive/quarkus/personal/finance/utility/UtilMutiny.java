package com.example.reactive.quarkus.personal.finance.utility;

import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.error.GenericError;
import io.smallrye.mutiny.Uni;

public final class UtilMutiny {
    private UtilMutiny() {
    }

    /**
     * Wraps a single item in a {@link Uni}.
     *
     * @param item the item to wrap
     * @param <T>  the type of the item
     * @return a {@link Uni} emitting the given item
     */
    public static <T> Uni<T> startUniFromItem(T item) {
        return Uni.createFrom().item(item);
    }

    public static <I, O> Uni<Either<Error, O>> createUniError(Either<Error, I> either) {
        return Uni.createFrom().item(Either.left(either.getLeft().orElse(new GenericError())));
    }
}
