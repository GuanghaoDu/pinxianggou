package cn.adu.domain.trade.model.entity;

import cn.adu.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPayOrderEntity {

        private String orderId;
        private BigDecimal deductionPrice;
        private TradeOrderStatusEnumVO tradeOrderStatusEnumVO;

}
