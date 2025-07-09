package cn.adu.domain.trade.model.aggregate;

import cn.adu.domain.trade.model.entity.MarketPayOrderEntity;
import cn.adu.domain.trade.model.entity.PayActivvityEntity;
import cn.adu.domain.trade.model.entity.PayDiscountEntity;
import cn.adu.domain.trade.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderAggregate {

        private UserEntity userEntity;

        private PayActivvityEntity payActivvityEntity;

        private PayDiscountEntity payDiscountEntity;

        private Integer userTakeOrderCount;
       // private MarketPayOrderEntity marketPayOrderEntity;
}
