package cn.adu.trigger.http;

import cn.adu.api.IMarketTradeService;
import cn.adu.api.dto.LockMarketPayOrderRequestDTO;
import cn.adu.api.dto.LockMarketPayOrderResponseDTO;
import cn.adu.api.response.Response;
import cn.adu.domain.activity.model.entity.MarketProductEntity;
import cn.adu.domain.activity.model.entity.TrialBalanceEntity;
import cn.adu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.adu.domain.activity.service.IIndexGroupBuyMarketService;
import cn.adu.domain.trade.model.entity.MarketPayOrderEntity;
import cn.adu.domain.trade.model.entity.PayActivvityEntity;
import cn.adu.domain.trade.model.entity.PayDiscountEntity;
import cn.adu.domain.trade.model.entity.UserEntity;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.adu.domain.trade.service.ITradeOrderService;
import cn.adu.domain.trade.service.TraderOrderService;
import cn.adu.types.enums.ResponseCode;
import cn.adu.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/trade")
public class MarketTradeController implements IMarketTradeService {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;
    @Resource
    private ITradeOrderService iTradeOrderService;
    @Autowired
    private TraderOrderService traderOrderService;

    @RequestMapping(value = "lock_market_pay_Order",method = RequestMethod.POST)
    @Override
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(@RequestBody LockMarketPayOrderRequestDTO lockMarketPayOrderRequsetDTO) {

       try {

           String userId=lockMarketPayOrderRequsetDTO.getUserId();
           String outTradeNo=lockMarketPayOrderRequsetDTO.getOutTradeNo();
           String channel=lockMarketPayOrderRequsetDTO.getChannel();
           String source=lockMarketPayOrderRequsetDTO.getSource();
           String teamId=lockMarketPayOrderRequsetDTO.getTeamId();
           Long activityId=lockMarketPayOrderRequsetDTO.getActivityId();
           String goodsId=lockMarketPayOrderRequsetDTO.getGoodsId();
           log.info("营销交易锁单:{} LockMarketPayOrderRequestDTO:{}", userId, JSON.toJSONString(lockMarketPayOrderRequsetDTO));

           if (StringUtils.isBlank(userId) || StringUtils.isBlank(source) || StringUtils.isBlank(channel) || StringUtils.isBlank(goodsId) || StringUtils.isBlank(goodsId) || null == activityId) {
               return Response.<LockMarketPayOrderResponseDTO>builder()
                       .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                       .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                       .build();
           }

           // 查询 outTradeNo 是否已经存在交易记录
           MarketPayOrderEntity marketPayOrderEntity = traderOrderService.queryNoPayMarketPayOrderByOutTradeNo(userId, outTradeNo);
           if (null != marketPayOrderEntity) {
               LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = LockMarketPayOrderResponseDTO.builder()
                       .orderId(marketPayOrderEntity.getOrderId())
                       .deductionPrice(marketPayOrderEntity.getDeductionPrice())
                       .tradeOrderStatus(marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode())
                       .build();

               log.info("交易锁单记录(存在):{} marketPayOrderEntity:{}", userId, JSON.toJSONString(marketPayOrderEntity));
               return Response.<LockMarketPayOrderResponseDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info(ResponseCode.SUCCESS.getInfo())
                       .data(lockMarketPayOrderResponseDTO)
                       .build();
           }

           // 判断拼团是否完成了目标
           if (null != teamId) {
               GroupBuyProgressVO groupBuyProgressVO = traderOrderService.queryGroupBuyProgress(teamId);
               if (null != groupBuyProgressVO && Objects.equals(groupBuyProgressVO.getTargetCount(), groupBuyProgressVO.getLockCount())) {
                   log.info("交易锁单拦截-拼单目标已达成:{} {}", userId, teamId);
                   return Response.<LockMarketPayOrderResponseDTO>builder()
                           .code(ResponseCode.E0006.getCode())
                           .info(ResponseCode.E0006.getInfo())
                           .build();
               }
           }

           // 营销优惠试算
           TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(MarketProductEntity.builder()
                   .userId(userId)
                   .source(source)
                   .channel(channel)
                   .goodsId(goodsId)
                   .activityId(activityId)
                   .build());
           // 人群限定
           if (!trialBalanceEntity.getIsVisible() || !trialBalanceEntity.getIsEnable()){
               return Response.<LockMarketPayOrderResponseDTO>builder()
                       .code(ResponseCode.E0007.getCode())
                       .info(ResponseCode.E0007.getInfo())
                       .build();
           }
           GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = trialBalanceEntity.getGroupBuyActivityDiscountVO();

           // 锁单
           marketPayOrderEntity = traderOrderService.lockMarketPayOrder(
                   UserEntity.builder().userId(userId).build(),
                   PayActivvityEntity.builder()
                           .teamId(teamId)
                           .activityId(activityId)
                           .activityName(groupBuyActivityDiscountVO.getActivityName())
                           .startTime(groupBuyActivityDiscountVO.getStartTime())
                           .endTime(groupBuyActivityDiscountVO.getEndTime())
                           .targetCount(groupBuyActivityDiscountVO.getTarget())
                           .build(),
                   PayDiscountEntity.builder()
                           .source(source)
                           .channel(channel)
                           .goodsId(goodsId)
                           .goodsName(trialBalanceEntity.getGoodsName())
                           .originalPrice(trialBalanceEntity.getOriginalPrice())
                           .deductionPrice(trialBalanceEntity.getDeductionPrice())
                           .payPrice(trialBalanceEntity.getPayPrice())
                           .outTradeNo(outTradeNo)
                           .build());

          log.info("交易锁单记录(新):{} marketPayOrderEntity:{}", userId, JSON.toJSONString(marketPayOrderEntity));

           // 返回结果
           return Response.<LockMarketPayOrderResponseDTO>builder()
                   .code(ResponseCode.SUCCESS.getCode())
                   .info(ResponseCode.SUCCESS.getInfo())
                   .data(LockMarketPayOrderResponseDTO.builder()
                           .orderId(marketPayOrderEntity.getOrderId())
                           .deductionPrice(marketPayOrderEntity.getDeductionPrice())
                           .tradeOrderStatus(marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode())
                           .build())
                   .build();

       }catch (AppException e){
           return Response.<LockMarketPayOrderResponseDTO>builder()
                   .code(e.getCode())
                   .info(e.getInfo())
                   .build();
       }catch (Exception e){

           return Response.<LockMarketPayOrderResponseDTO>builder()
                   .code(ResponseCode.UN_ERROR.getCode())
                   .info(ResponseCode.UN_ERROR.getInfo())
                   .build();

       }



    }
}
