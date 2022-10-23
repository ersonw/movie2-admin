package com.example.movie2admin.data;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
public class Dashboard {
    private Double cashInAll=0D,
    cashInToday=0D, cashInYesterday=0D, cashOutAll=0D, cashOutToday=0D, cashOutYesterday=0D,cashInGameAll=0D, cashInDiamondAll=0D,
            cashInMembershipAll=0D, cashInGameToday=0D, cashInDiamondToday=0D, cashInMembershipToday=0D, cashInGameYesterday=0D,
            cashInMembershipYesterday=0D, cashInDiamondYesterday=0D;
    private Long cashOutOrders=0L, usersAll=0L, usersToday=0L, usersYesterday=0L, membershipAll=0L, membershipToday=0L,
    membershipYesterday=0L, videosAll=0L, videosToday=0L, videosYesterday=0L, shortAll=0L, shortToday=0L, shortAudit=0L,
    shortYesterday=0L,  online=0L;

    @Override
    public String toString() {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(this));
        for (Map.Entry<String, Object> entry: object.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof Double) {
                object.put(entry.getKey(),String.format("%.2f", entry.getValue()));
            }
        }
        return object.toJSONString();
    }
}
