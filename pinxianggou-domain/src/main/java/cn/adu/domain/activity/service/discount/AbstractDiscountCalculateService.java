package cn.adu.domain.activity.service.discount;

import cn.adu.domain.activity.adapter.repository.IActivityRepository;
import cn.adu.domain.activity.model.valobj.DiscountTypeEnum;
import cn.adu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @description 折扣计算服务抽象类
 * @create 2024-12-22 12:32
 */
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {
    @Resource
    protected IActivityRepository repository;
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        // 1. 人群标签过滤
        if (DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if (!isCrowdRange) return originalPrice;
        }
        // 2. 折扣优惠计算
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    // 人群过滤 - 限定人群优惠
    private boolean filterTagId(String userId, String tagId) {

        return repository.isTagCrowdRange(tagId,userId);
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}
