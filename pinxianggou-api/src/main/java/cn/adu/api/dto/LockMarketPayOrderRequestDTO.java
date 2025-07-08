package cn.adu.api.dto;

import lombok.Data;

@Data
public class LockMarketPayOrderRequestDTO {

    private String userId;
    private String outTradeNo;
    private String teamId;
    private Long activityId;
    private String source;
    private String channel;
    private String goodsId;
}
