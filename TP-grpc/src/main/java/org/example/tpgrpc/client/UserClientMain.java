package org.example.tpgrpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.UserId;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;
import org.example.grpc.UserServiceGrpc;

public class UserClientMain {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        try {
            UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);

            System.out.println("== createUser ==");
            UserResponse created = stub.createUser(
                    UserRequest.newBuilder()
                            .setName("Alice")
                            .setEmail("alice@example.com")
                            .build()
            );
            System.out.println("Created: " + created);

            System.out.println("== getUser ==");
            UserResponse found = stub.getUser(
                    UserId.newBuilder().setId(created.getId()).build()
            );
            System.out.println("Found: " + found);

            System.out.println("== getAllUsers ==");
            System.out.println("OK.");

        } finally {
            channel.shutdown();
        }
    }
}
