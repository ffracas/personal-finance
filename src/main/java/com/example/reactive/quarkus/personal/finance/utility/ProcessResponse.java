package com.example.reactive.quarkus.personal.finance.utility;

import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.error.*;
import com.example.reactive.quarkus.personal.finance.model.success.Success;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public final class ProcessResponse {
    private ProcessResponse() {
    }

    public static Uni<Response> processEmptyResponse(Uni<Either<Error, Success>> response) {
        return response.flatMap(either ->
                switch (either) {
                    case Either.Left<Error, Success> left -> checkLeft(left.getLeft().orElse(new GenericError()));
                    case Either.Right<Error, Success> ignored -> Uni.createFrom().item(Response.noContent().build());
                });
    }

    public static <T> Uni<Response> processTheResultFromService(Uni<Either<Error, T>> response, Response.Status status) {
        return response.flatMap(either -> switch (either) {
            case Either.Left<Error, T> left -> checkLeft(left.getLeft().orElse(new GenericError()));
            case Either.Right<Error, T> right ->
                    Uni.createFrom().item(Response.status(status).entity(right.getRight()).build());
        });
    }

    private static Uni<Response> checkLeft(Error error) {
        return switch (error) {
            case BondError bondError -> handlerBondError(bondError);
            case InvestmentError investmentError -> handlerInvestmentError(investmentError);
            case RecurringExpenseError recurringExpenseError -> handlerRecurringExpenseError(recurringExpenseError);
            case TransactionError transactionError -> handlerTransactionError(transactionError);
            case UserError userError -> handlerUserError(userError);
            case GenericError ignored -> createMonoError(Response.Status.INTERNAL_SERVER_ERROR, ignored);
        };
    }

    private static Uni<Response> handlerTransactionError(Error error) {
        return switch (error) {
            case TransactionNotFound transactionNotFound ->
                    createMonoError(Response.Status.NOT_FOUND, transactionNotFound);
            case TransactionServerError transactionServerError ->
                    createMonoError(Response.Status.INTERNAL_SERVER_ERROR, transactionServerError);
            default -> createMonoError(Response.Status.BAD_REQUEST, new GenericError());
        };
    }

    private static Uni<Response> handlerUserError(Error error) {
        return switch (error) {
            case UserNotFound userNotFound -> createMonoError(Response.Status.NOT_FOUND, userNotFound);
            case UserServerError userServerError ->
                    createMonoError(Response.Status.INTERNAL_SERVER_ERROR, userServerError);
            default -> createMonoError(Response.Status.BAD_REQUEST, new GenericError());
        };
    }

    private static Uni<Response> handlerRecurringExpenseError(Error error) {
        return switch (error) {
            case RecurringExpenseNotFound recurringExpenseNotFound ->
                    createMonoError(Response.Status.NOT_FOUND, recurringExpenseNotFound);
            case RecurringExpenseServerError recurringExpenseServerError ->
                    createMonoError(Response.Status.INTERNAL_SERVER_ERROR, recurringExpenseServerError);
            default -> createMonoError(Response.Status.BAD_REQUEST, new GenericError());
        };
    }

    private static Uni<Response> handlerInvestmentError(Error error) {
        return switch (error) {
            case InvestmentNotFound investmentNotFound ->
                    createMonoError(Response.Status.NOT_FOUND, investmentNotFound);
            case InvestmentServerError investmentServerError ->
                    createMonoError(Response.Status.INTERNAL_SERVER_ERROR, investmentServerError);
            default -> createMonoError(Response.Status.BAD_REQUEST, new GenericError());
        };
    }

    private static Uni<Response> handlerBondError(Error error) {
        return switch (error) {
            case BondNotFound bondNotFound -> createMonoError(Response.Status.NOT_FOUND, bondNotFound);
            case BondServerError bondServerError ->
                    createMonoError(Response.Status.INTERNAL_SERVER_ERROR, bondServerError);
            default -> createMonoError(Response.Status.BAD_REQUEST, new GenericError());
        };
    }

    /**
     * Creates a {@link Uni} that emits an error response with the given HTTP status and error message.
     *
     * <p>Example:
     * <pre>
     *   createMonoError(Response.Status.NOT_FOUND, new PersonNotFound());
     *   // Returns: 404 Not Found response with PersonNotFound error message
     * </pre>
     *
     * @param httpStatus the HTTP status for the error response
     * @param message    the error message to include in the response
     * @return a {@link Uni} containing the error response
     */
    private static Uni<Response> createMonoError(Response.Status httpStatus, Error message) {
        return Uni.createFrom()
                .item(Response.status(httpStatus)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(message)
                        .build());
    }
}

