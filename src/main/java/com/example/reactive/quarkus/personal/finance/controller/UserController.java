package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.UserRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.UserResponseDto;
import com.example.reactive.quarkus.personal.finance.service.UserService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.Set;

/**
 * <p>
 * The {@code UserController} class is a RESTful resource that exposes endpoints
 * to perform CRUD operations on user entities. It leverages reactive programming
 * paradigms (using Mutiny's {@code Uni} and {@code Multi}) to support non-blocking
 * and asynchronous processing.
 * </p>
 * <p>
 * All endpoints under this controller are mapped to the base path {@code /user}.
 * The controller delegates business logic to an injected {@link UserService} instance.
 * The service layer is responsible for interacting with the data repository and applying
 * any business-specific rules or transformations.
 * </p>
 * <p>
 * <strong>Endpoints provided:</strong>
 * <ul>
 *     <li>{@code GET /user/getUser/{userId}} - Retrieves a specific user's details by ID.</li>
 *     <li>{@code GET /user/getAllUser} - Retrieves all users encapsulated in a {@code Set}.</li>
 *     <li>{@code POST /user/createUser} - Creates a new user from the provided request data.</li>
 *     <li>{@code PUT /user/updateUser} - Updates an existing user's details.</li>
 * </ul>
 * </p>
 *
 * @see UserService
 * @see UserRequestDto
 * @see UserResponseDto
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Management", description = "Operations related to user management")
public final class UserController {

    private final UserService userService;

    /**
     * Constructs a new {@code UserController} with the specified {@code UserService}.
     *
     * @param userService the service used to perform business operations related to users;
     *                    must not be {@code null}.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user's details by their unique identifier.
     * <p>
     * This endpoint responds to HTTP GET requests at {@code /user/getUser/{userId}}.
     * It delegates the retrieval operation to the {@code UserService} and returns
     * the result as a reactive {@code Uni<UserResponseDto>} which will asynchronously
     * yield the user data.
     * </p>
     *
     * @param userId the unique identifier of the user to be retrieved.
     *               This value is extracted from the URL path parameter.
     * @return a {@code Uni<UserResponseDto>} that, when subscribed to, will emit the
     * user details corresponding to the given {@code userId}.
     * @see UserService#getUserById(String)
     */
    @Operation(summary = "Retrieve user details by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    @GET
    @Path("/getUser/{userId}")
    public Uni<Response> getUserById(@PathParam("userId") String userId) {
        return userService.getUserById(userId)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Retrieves a collection of all users.
     * <p>
     * This endpoint handles HTTP GET requests at {@code /user/getAllUser}. It returns
     * a reactive stream (a {@code Multi}) that emits a {@code Set<UserResponseDto>}.
     * This design allows for non-blocking retrieval of user data, supporting scalability
     * in high-throughput environments.
     * </p>
     *
     * @return a {@code Multi<Set<UserResponseDto>>} that, when subscribed to, will emit a set
     * of user response DTOs containing details for all users.
     * @see UserService#getAllUser()
     */
    @Operation(summary = "Retrieve all users")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = UserResponseDto.class))),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    @GET
    @Path("/getAllUser")
    public Multi<Set<UserResponseDto>> getAllUser() {
        return userService.getAllUser();
    }

    /**
     * Creates a new user based on the provided request data.
     * <p>
     * This endpoint listens for HTTP POST requests at {@code /user/createUser}.
     * The request payload should conform to the {@code UserRequestDto} structure.
     * The controller delegates the creation process to the {@code UserService} and
     * returns the newly created user's details as a reactive {@code Uni<UserResponseDto>}.
     * </p>
     *
     * @param userRequestDto the data transfer object containing the new user's details.
     *                       It must be provided in the request body in a JSON format.
     * @return a {@code Uni<UserResponseDto>} that, upon subscription, will emit the
     * response DTO representing the newly created user.
     * @see UserService#createUser(UserRequestDto)
     */
    @ResponseStatus(201)
    @Operation(summary = "Create a new user")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    @POST
    @Path("/createUser")
    public Uni<Response> createUser(UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto)
                .map(user -> Response.ok(user).status(Response.Status.CREATED).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST).build());
    }

    /**
     * Updates an existing user's details.
     * <p>
     * This endpoint accepts HTTP PUT requests at {@code /user/updateUser}.
     * The request body must contain the updated user details conforming to the
     * {@code UserRequestDto} structure. The controller passes the update request
     * to the {@code UserService} and returns the updated user data as a reactive
     * {@code Uni<UserResponseDto>}.
     * </p>
     *
     * @param userRequestDto the data transfer object containing updated user details.
     *                       It should be included in the request body in JSON format.
     * @return a {@code Uni<UserResponseDto>} that, when subscribed to, emits the
     * updated user details.
     * @see UserService#updateUser(UserRequestDto, String)
     */
    @Operation(summary = "Update an existing user")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "404", description = "User not found"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    @PUT
    @Path("/updateUser/{userId}")
    public Uni<Response> updateUser(UserRequestDto userRequestDto, @PathParam("userId") String userId) {
        return userService.updateUser(userRequestDto, userId)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/deleteUser/{userId}")
    public Uni<Response> deleteUser(@PathParam("userId") String userId) {
        return userService.deleteUser(userId)
                .map(response -> response ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
