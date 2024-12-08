package com.switchmaker.smpay.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.switchmaker.smpay.constant.urls.GlobalConstantUrls.CROSS_ORIGIN;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins(CROSS_ORIGIN)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/socket-subscriber")
                .enableSimpleBroker(
                		"/socket-publisher/demande",
                		"/socket-publisher/demande/treat",
                		"/socket-publisher/demande/reject",
                		"/socket-publisher/client",
                		"/socket-publisher/client/deleted",
                		"/socket-publisher/platform",
                		"/socket-publisher/platform/deleted",
                		"/socket-publisher/contact",
                		"/socket-publisher/notification",
                		"/socket-publisher/notification/deleted");
    }

}
