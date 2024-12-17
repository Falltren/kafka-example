package com.fallt.notifications.service;

import com.fallt.notifications.domain.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendNotification(Order order) {
        log.info("Dear customer, your order: {} has been delivered", order.getUuid());
    }
}
