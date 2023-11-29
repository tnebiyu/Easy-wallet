//package com.nebiyu.Kelal.configuration;
//
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        // Enable a simple message broker that will route messages to "/topic" for subscribers
//        config.enableSimpleBroker("/topic");
//        // Set the prefix for messages that are sent from clients to the server
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // Register the "/ws" endpoint for WebSocket communication
//        // The SockJS library is used to enable fallback options for browsers that do not support WebSocket
//        registry.addEndpoint("/ws").withSockJS();
//    }
//}
