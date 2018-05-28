package com.cdkj.cdroom.controller;

import com.cdkj.base.controller.BaseController;
import com.cdkj.cdroom.constant.Constant;
import com.cdkj.cdroom.model.pojo.BasCouponUse;
import com.cdkj.cdroom.model.pojo.ExpUser;
import com.cdkj.cdroom.service.api.BasCouponUseService;
import com.cdkj.cdroom.service.api.ExpUserService;
import com.cdkj.common.exception.CustException;
import com.cdkj.common.util.DateUtil;
import com.cdkj.common.util.RetUtils;
import com.cdkj.common.util.StringUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haing on 2017/3/1.
 */
@Controller("basCouponUseController")
@RequestMapping("/userCoupon")
public class BasCouponUseController extends BaseController {
     @Resource
    private BasCouponUseService basCouponUseService;
     @Resource
    private ExpUserService expUserService;


    /**
     * 查询优惠券list或者优惠券总额
     *
     * @param expUserId #乐存用户id
     * @param channelId #渠道
     * @param type      1、可以使用 2 已使用完 3 已过期 null 所有
     * @param qryFlag   1只查询优惠券总额 2 只查询优惠券list  3 都查询
     * @return qryFlag=1 返回优惠券总额sum   qryFlag=2  返回优惠券list   其他都返回
     */
    @ResponseBody
    @RequestMapping(value = "/qryCoupon", method = RequestMethod.GET)
    public String qryCoupon(String expUserId, String channelId, Short type, Short qryFlag) {
        if (!StringUtil.areNotEmpty(expUserId, channelId)) {
            return RetUtils.resFailed("参数传递错误,请核对信息!");
        }
        try {
            //SysUser sysUser = sysUserService.queryByUserNo(userNo, channelId);
            ExpUser expUser = expUserService.qryById(expUserId);
            if (null == expUser || !StringUtil.equals(expUser.getUsrType(), Constant.EXP_USER_TYPE_LECUN)) {
                return RetUtils.resFailed("用户不存在");
            }
            Map ret = new HashMap<>();
            if (null == qryFlag || 3 == qryFlag) {
                List<BasCouponUse> list = basCouponUseService.qryByUserId(expUserId, type);
                BigDecimal sum = basCouponUseService.qrySum(expUserId);
                ret.put("list", list == null ? new ArrayList<>() : list);
                ret.put("sum", sum);
            } else if (1 == qryFlag) {
//                List<BasCouponUse> list = basCouponUseService.qryByUserId(sysUser.getUserId(), type);
                BigDecimal sum = basCouponUseService.qrySum(expUserId);
//                ret.put("list", new ArrayList<>());
                ret.put("sum", sum);
            } else if (2 == qryFlag) {
                List<BasCouponUse> list = basCouponUseService.qryByUserId(expUserId, type);
//                BigDecimal sum = basCouponUseService.qrySum(sysUser.getUserId());
                ret.put("list", list == null ? new ArrayList<>() : list);
//                ret.put("sum", sum);
            } else {
                List<BasCouponUse> list = basCouponUseService.qryByUserId(expUserId, type);
                BigDecimal sum = basCouponUseService.qrySum(expUserId);
                ret.put("list", list == null ? new ArrayList<>() : list);
                ret.put("sum", sum);
            }
            ret.put("now", DateUtil.getNow());
            return RetUtils.res(ret, BasCouponUse.class, BasCouponUse.INCLUDES);
        } catch (CustException ce) {
            logger.error("BasExpressController.qryExpress.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BasExpressController.qryExpress.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 查询乐存用户优惠券数量和钱
     *
     * @param expUserId #乐存用户id
     * @return couponCnt有效的优惠券数量，couponSum有效的优惠券的总金额
     */
    @ResponseBody
    @RequestMapping(value = "/qryCouponCnt", method = RequestMethod.GET)
    public String qryCouponCnt(String expUserId) {
        try {
            if (!StringUtil.areNotEmpty(expUserId)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            ExpUser expUser = expUserService.qryById(expUserId);
            if (null == expUser|| !StringUtil.equals(expUser.getUsrType(), Constant.EXP_USER_TYPE_LECUN)) {
                return RetUtils.resFailed("用户不存在!");
            }
            int couponCnt = basCouponUseService.qryCnt(expUserId, (short) 1);
            BigDecimal couponSum = basCouponUseService.qrySum(expUserId);
            Map ret = new HashMap<>();
            ret.put("couponCnt", couponCnt);//有效的优惠券数量
            ret.put("couponSum", couponSum);//有效的优惠券的总金额
            return RetUtils.res(ret);
        } catch (CustException ce) {
            logger.error("basCouponUseController.qryCouponCnt.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("basCouponUseController.qryCouponCnt.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }
}
