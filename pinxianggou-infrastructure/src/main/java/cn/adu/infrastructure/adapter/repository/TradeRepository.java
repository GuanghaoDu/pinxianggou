package cn.adu.infrastructure.adapter.repository;

import cn.adu.domain.trade.adapter.repository.ITradeRepository;
import cn.adu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.adu.domain.trade.model.entity.*;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.adu.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import cn.adu.infrastructure.dao.IGroupBuyActivityDao;
import cn.adu.infrastructure.dao.IGroupBuyOrderDao;
import cn.adu.infrastructure.dao.IGroupBuyOrderListDao;
import cn.adu.infrastructure.dao.po.GroupBuyActivity;
import cn.adu.infrastructure.dao.po.GroupBuyOrder;
import cn.adu.infrastructure.dao.po.GroupBuyOrderList;
import cn.adu.types.common.Constants;
import cn.adu.types.enums.ActivityStatusEnumVO;
import cn.adu.types.enums.ResponseCode;
import cn.adu.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Repository
public class TradeRepository implements ITradeRepository {

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {

        GroupBuyOrderList groupBuyOrderList = new GroupBuyOrderList();
        groupBuyOrderList.setUserId(userId);
        groupBuyOrderList.setOutTradeNo(outTradeNo);
        GroupBuyOrderList groupBuyOrderList1 = groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderList);

        if (null==groupBuyOrderList1) return null;
      return MarketPayOrderEntity.builder()
                .orderId(groupBuyOrderList1.getUserId())
                .deductionPrice(groupBuyOrderList1.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderList1.getStatus()))
                .build();
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        GroupBuyOrder groupBuyOrder=groupBuyOrderDao.queryGroupBuyProgress(teamId);
        if (null==groupBuyOrder)return null;

        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {

        UserEntity userEntity=groupBuyOrderAggregate.getUserEntity();
        PayActivvityEntity payActivvityEntity=groupBuyOrderAggregate.getPayActivvityEntity();
        PayDiscountEntity payDiscountEntity=groupBuyOrderAggregate.getPayDiscountEntity();
        Integer userTakeOrderCount = groupBuyOrderAggregate.getUserTakeOrderCount();
        String teamId = payActivvityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)){

            teamId= RandomStringUtils.randomNumeric(8);

            GroupBuyOrder groupBuyOrder =GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivvityEntity.getActivityId())
                    .source(payDiscountEntity.getSource())
                    .channel(payDiscountEntity.getChannel())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .payPrice(payDiscountEntity.getDeductionPrice().subtract(payDiscountEntity.getDeductionPrice()))
                    .completeCount(payActivvityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        }else {
            int updateAddLockCount = groupBuyOrderDao.updateAddLockCount(teamId);
            if (updateAddLockCount!=1){
                throw new AppException(ResponseCode.E0005);

            }
        }
        String orderId=RandomStringUtils.randomNumeric(12);
        GroupBuyOrderList groupBuyOrderList = GroupBuyOrderList.builder()
                .userId(userEntity.getUserId())
                .teamId(teamId)
                .activityId(payActivvityEntity.getActivityId())
                .startTime(payActivvityEntity.getStartTime())
                .endTime(payActivvityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .source(payDiscountEntity.getSource())
                .channel(payDiscountEntity.getChannel())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .bizId(payActivvityEntity.getActivityId()+ Constants.UNDERLINE+userEntity.getUserId()+Constants.UNDERLINE+(userTakeOrderCount+1))
                .build();

        try{
            groupBuyOrderListDao.insert(groupBuyOrderList);
        }catch (DuplicateKeyException e){
            throw new AppException(ResponseCode.INDEX_EXCEPTION);
        }

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .build();

    }



    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId) {

        GroupBuyActivity groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(ActivityStatusEnumVO.valueof(groupBuyActivity.getStatus()))
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();

    }
    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {

        GroupBuyOrderList groupBuyOrderListReq=new GroupBuyOrderList();
        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setUserId(userId);
        return groupBuyOrderListDao.queryOrderCountByActivityId(groupBuyOrderListReq);

    }
}
