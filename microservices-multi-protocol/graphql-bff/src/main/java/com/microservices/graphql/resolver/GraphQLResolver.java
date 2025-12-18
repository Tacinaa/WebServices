package com.microservices.graphql.resolver;

import com.microservices.graphql.client.PaymentGrpcClient;
import com.microservices.graphql.model.PaymentModel;
import com.microservices.grpc.order.CreateOrderItemRequest;
import com.microservices.graphql.client.OrderGrpcClient;
import com.microservices.graphql.client.ProductGrpcClient;
import com.microservices.graphql.client.UserGrpcClient;
import com.microservices.graphql.model.OrderModel;
import com.microservices.graphql.model.ProductModel;
import com.microservices.graphql.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.grpc.PaymentResponse;
import org.example.paymentservice.grpc.PaymentStatus;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 🔮 GRAPHQL RESOLVERS
 * 
 * Agrège les données des microservices via gRPC (appels synchrones)
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class GraphQLResolver {

    private final UserGrpcClient userClient;
    private final ProductGrpcClient productClient;
    private final OrderGrpcClient orderClient;
    private final PaymentGrpcClient paymentClient;

    // ==================== USER QUERIES ====================
    
    @QueryMapping
    public List<UserModel> users() {
        log.info("🔮 [GraphQL] Query: users");
        return userClient.getAllUsers();
    }

    @QueryMapping
    public UserModel user(@Argument Long id) {
        log.info("🔮 [GraphQL] Query: user({})", id);
        return userClient.getUser(id).orElse(null);
    }

    @QueryMapping
    public UserModel userByUsername(@Argument String username) {
        log.info("🔮 [GraphQL] Query: userByUsername({})", username);
        return userClient.getUserByUsername(username).orElse(null);
    }

    // ==================== PRODUCT QUERIES ====================
    
    @QueryMapping
    public List<ProductModel> products() {
        log.info("🔮 [GraphQL] Query: products");
        return productClient.getAllProducts();
    }

    @QueryMapping
    public ProductModel product(@Argument Long id) {
        log.info("🔮 [GraphQL] Query: product({})", id);
        return productClient.getProduct(id).orElse(null);
    }

    @QueryMapping
    public List<ProductModel> productsByCategory(@Argument String category) {
        log.info("🔮 [GraphQL] Query: productsByCategory({})", category);
        return productClient.getProductsByCategory(category);
    }

    // ==================== ORDER QUERIES ====================
    
    @QueryMapping
    public List<OrderModel> orders() {
        log.info("🔮 [GraphQL] Query: orders");
        return orderClient.getAllOrders();
    }

    @QueryMapping
    public OrderModel order(@Argument Long id) {
        log.info("🔮 [GraphQL] Query: order({})", id);
        return orderClient.getOrder(id).orElse(null);
    }

    @QueryMapping
    public List<OrderModel> ordersByUser(@Argument Long userId) {
        log.info("🔮 [GraphQL] Query: ordersByUser({})", userId);
        return orderClient.getOrdersByUser(userId);
    }

    @QueryMapping
    public PaymentModel payment(@Argument Long id) {
        log.info("🔮 [GraphQL] Query: payment({})", id);
        return toPaymentModel(paymentClient.getPayment(id));
    }

    @QueryMapping
    public List<PaymentModel> paymentsByOrder(@Argument Long orderId) {
        log.info("🔮 [GraphQL] Query: paymentsByOrder({})", orderId);
        return paymentClient.getPaymentsByOrder(orderId)
                .stream()
                .map(this::toPaymentModel)
                .toList();
    }

    // ==================== FIELD RESOLVERS ====================
    
    @SchemaMapping(typeName = "User", field = "orders")
    public List<OrderModel> userOrders(UserModel user) {
        log.info("🔮 [GraphQL] Resolving User.orders for user {}", user.getId());
        return orderClient.getOrdersByUser(user.getId());
    }

    @SchemaMapping(typeName = "Order", field = "user")
    public UserModel orderUser(OrderModel order) {
        log.info("🔮 [GraphQL] Resolving Order.user for order {}", order.getId());
        return userClient.getUser(order.getUserId()).orElse(null);
    }

    // ==================== USER MUTATIONS ====================
    
    @MutationMapping
    public UserModel createUser(@Argument Map<String, Object> input) {
        log.info("🔮 [GraphQL] Mutation: createUser");
        return userClient.createUser(
                (String) input.get("username"),
                (String) input.get("email"),
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                (String) input.get("password")
        );
    }

    @MutationMapping
    public UserModel updateUser(@Argument Long id, @Argument Map<String, Object> input) {
        log.info("🔮 [GraphQL] Mutation: updateUser({})", id);
        return userClient.updateUser(id,
                (String) input.get("email"),
                (String) input.get("firstName"),
                (String) input.get("lastName")
        );
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        log.info("🔮 [GraphQL] Mutation: deleteUser({})", id);
        return userClient.deleteUser(id);
    }

    // ==================== PRODUCT MUTATIONS ====================
    
    @MutationMapping
    public ProductModel createProduct(@Argument Map<String, Object> input) {
        log.info("🔮 [GraphQL] Mutation: createProduct");
        return productClient.createProduct(
                (String) input.get("name"),
                (String) input.get("description"),
                ((Number) input.get("price")).doubleValue(),
                ((Number) input.get("stock")).intValue(),
                (String) input.get("category")
        );
    }

    @MutationMapping
    public Boolean updateStock(@Argument Long id, @Argument Integer quantity) {
        log.info("🔮 [GraphQL] Mutation: updateStock({}, {})", id, quantity);
        return productClient.updateStock(id, quantity);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        log.info("🔮 [GraphQL] Mutation: deleteProduct({})", id);
        return productClient.deleteProduct(id);
    }

    // ==================== ORDER MUTATIONS ====================
    
    @SuppressWarnings("unchecked")
    @MutationMapping
    public OrderModel createOrder(@Argument Map<String, Object> input) {
        log.info("🔮 [GraphQL] Mutation: createOrder");
        
        Long userId = ((Number) input.get("userId")).longValue();
        String shippingAddress = (String) input.get("shippingAddress");
        List<Map<String, Object>> itemsInput = (List<Map<String, Object>>) input.get("items");
        
        List<CreateOrderItemRequest> items = itemsInput.stream()
                .map(item -> CreateOrderItemRequest.newBuilder()
                        .setProductId(((Number) item.get("productId")).longValue())
                        .setQuantity(((Number) item.get("quantity")).intValue())
                        .build())
                .collect(Collectors.toList());
        
        return orderClient.createOrder(userId, items, shippingAddress);
    }

    @MutationMapping
    public OrderModel updateOrderStatus(@Argument Long id, @Argument String status) {
        log.info("🔮 [GraphQL] Mutation: updateOrderStatus({}, {})", id, status);
        return orderClient.updateOrderStatus(id, status);
    }

    @MutationMapping
    public Boolean cancelOrder(@Argument Long id) {
        log.info("🔮 [GraphQL] Mutation: cancelOrder({})", id);
        return orderClient.cancelOrder(id);
    }

    @MutationMapping
    public PaymentModel processPayment(@Argument Map<String, Object> input) {
        log.info("🔮 [GraphQL] Mutation: processPayment");

        return toPaymentModel(
                paymentClient.processPayment(
                        ((Number) input.get("orderId")).longValue(),
                        ((Number) input.get("amount")).doubleValue(),
                        (String) input.get("currency"),
                        (String) input.get("paymentMethod")
                )
        );
    }

    @MutationMapping
    public PaymentModel updatePaymentStatus(@Argument Long id, @Argument String status) {
        log.info("🔮 [GraphQL] Mutation: updatePaymentStatus({}, {})", id, status);
        return toPaymentModel(
                paymentClient.updatePaymentStatus(id, PaymentStatus.valueOf(status))
        );
    }

    @MutationMapping
    public PaymentModel refundPayment(@Argument Long id) {
        log.info("🔮 [GraphQL] Mutation: refundPayment({})", id);
        return toPaymentModel(paymentClient.refundPayment(id));
    }

    private PaymentModel toPaymentModel(PaymentResponse p) {
        PaymentModel m = new PaymentModel();
        m.setId(p.getId());
        m.setOrderId(p.getOrderId());
        m.setAmount(p.getAmount());
        m.setCurrency(p.getCurrency());
        m.setStatus(p.getStatus().name());
        m.setPaymentMethod(p.getPaymentMethod());
        m.setTransactionId(p.getTransactionId());
        m.setCreatedAt(p.getCreatedAt());
        m.setUpdatedAt(p.getUpdatedAt());
        return m;
    }
}
