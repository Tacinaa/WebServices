package org.example.paymentservice.grpc.client;

import com.microservices.grpc.order.GetOrderRequest;
import com.microservices.grpc.order.OrderServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class OrderGrpcClient {

    @GrpcClient("order-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderStub;

    public boolean orderExists(long orderId) {
        GetOrderRequest request = GetOrderRequest.newBuilder()
                .setId(orderId)
                .build();

        try {
            orderStub.getOrder(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}