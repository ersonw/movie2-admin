package com.example.movie2admin.util;

import com.example.movie2admin.entity.CashInOrder;
import com.example.movie2admin.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configurable
@Component
@Slf4j
public class EPayUtil {
    public static final int MEMBERSHIP_ORDER = 1;
    public static final int CASH_ORDER = 2;
    public static final int DIAMOND_ORDER = 3;
    public static final int GAME_ORDER = 4;
    private static EPayUtil self;
    @Autowired
    private GameService gameService;
    @PostConstruct
    public void init(){
        self = this;
    }
    public static boolean handlerOrder(CashInOrder cOrder) {
        switch (cOrder.getOrderType()){
            case CASH_ORDER:
                return false;
            case DIAMOND_ORDER:
                return false;
            case GAME_ORDER:
                return self.gameService.handlerOrder(cOrder.getOrderNo());
            case MEMBERSHIP_ORDER:
                return false;
            default:
                return false;
        }
    }
}
