package cn.adu.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderList {

    private String userId;

    private String teamId;

    private Long activityId;

    private Date startTime;

    private Date endTime;

    private String goodsId;

    private String source;

    private String channel;

    private BigDecimal originalPrice;

    private BigDecimal deductionPrice;

    private String outTradeNo;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
