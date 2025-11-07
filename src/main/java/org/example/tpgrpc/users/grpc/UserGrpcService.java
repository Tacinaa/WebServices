package org.example.tpgrpc.users.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.*;
import org.example.tpgrpc.users.domain.User;
import org.example.tpgrpc.users.service.UserService;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    @Override
    public void createUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            String name = request.getName().trim();
            String email = request.getEmail().trim();

            if (name.isEmpty() || email.isEmpty()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Les champs 'name' et 'email' sont requis.")
                                .asRuntimeException()
                );
                return;
            }

            User saved = userService.createUser(name, email);

            UserResponse response = UserResponse.newBuilder()
                    .setId(saved.getId())
                    .setName(saved.getName())
                    .setEmail(saved.getEmail())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalStateException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Erreur interne : " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getUser(UserId request, StreamObserver<UserResponse> responseObserver) {
        try {
            User found = userService.getUserById(request.getId());

            UserResponse response = UserResponse.newBuilder()
                    .setId(found.getId())
                    .setName(found.getName())
                    .setEmail(found.getEmail())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Erreur interne : " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        try {
            var users = userService.getAllUsers();

            UserList.Builder list = UserList.newBuilder();
            for (var u : users) {
                list.addUsers(
                        UserResponse.newBuilder()
                                .setId(u.getId())
                                .setName(u.getName())
                                .setEmail(u.getEmail())
                                .build()
                );
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Erreur interne : " + e.getMessage()).asRuntimeException()
            );
        }
    }

    public void deleteUser(UserId request, StreamObserver<DeleteResponse> responseObserver) {
        try {
            userService.deleteUser(request.getId());

            DeleteResponse resp = DeleteResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Utilisateur supprimé")
                    .build();

            responseObserver.onNext(resp);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Erreur interne : " + e.getMessage()).asRuntimeException()
            );
        }
    }
}