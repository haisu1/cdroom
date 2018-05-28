package com.cdkj.cdroom.controller;

import com.cdkj.base.controller.BaseController;
import com.cdkj.cdroom.constant.Constant;
import com.cdkj.cdroom.model.pojo.*;
import com.cdkj.cdroom.service.api.BasShelfService;
import com.cdkj.cdroom.service.api.SysGroupNodeService;
import com.cdkj.cdroom.service.api.SysUserService;
import com.cdkj.common.exception.CustException;
import com.cdkj.common.util.RetUtils;
import com.cdkj.common.util.StringUtil;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Package com.cdkj.cdroom.controller
 * @Author huangwen
 * @Date 2017-02-28
 */
@Controller("baseShelfController")
@RequestMapping("/baseShelf")
public class BaseShelfController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private BasShelfService basShelfService;

    @Resource
    private SysGroupNodeService sysGroupNodeService;

    /**
     * 添加货架号
     *
     * @param userNo
     * @param channelId
     * @param basShelfList
     * @return
     */
    @RequestMapping(value = "/addShelf", method = RequestMethod.POST)
    @ResponseBody
    public String addShelf(String userNo, String channelId, String basShelfList) {
        try {
            if (!StringUtil.areNotEmpty(userNo, channelId, basShelfList)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
            if (null == sysUser) {
                return RetUtils.resFailed("用户不存在");
            }
            return RetUtils.res(basShelfService.addShelf(sysUser, basShelfList));
        } catch (CustException ce) {
            logger.error("BaseShelfController.addShelf.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.addShelf.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }


    /**
     * 添加货架地址
     *
     * @param userNo
     * @param channelId
     * @param shelfAddr 货架地址
     * @return
     */
    @RequestMapping(value = "/addShelfAddr", method = RequestMethod.POST)
    @ResponseBody
    public String addShelfAddr(String userNo, String channelId, String shelfAddr) {
        try {
            if (!StringUtil.areNotEmpty(userNo, channelId, shelfAddr)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
            if (null == sysUser) {
                return RetUtils.resFailed("用户不存在");
            }
            return RetUtils.res(basShelfService.addShelfAddr(sysUser, shelfAddr), BasShelfAddr.class, BasShelfAddr.INCLUDES);
        } catch (CustException ce) {
            logger.error("BaseShelfController.addShelfAddr.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.addShelfAddr.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 查询货架地址
     *
     * @param userNo
     * @param channelId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qryShelfName", method = RequestMethod.GET)
    public String qryShelfName(String userNo, String nodeId, String channelId) {
        try {
//            if (!StringUtil.areNotEmpty(userNo, channelId)) {
//                return RetUtils.resFailed("参数传递错误,请核对信息!");
//            }
            String sysAccount = "";
            if (StringUtil.isNotEmpty(userNo)) {
                SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
                if (null != sysUser) {
                    //return RetUtils.resFailed("用户不存在");
                    sysAccount = sysUser.getSysAccount();
                }
            }
            if (StringUtil.isNotEmpty(nodeId)) {
                SysGroupNode node = sysGroupNodeService.selectByPrimaryKey(nodeId);
                if (null != node) {
                    //return RetUtils.resFailed("用户不存在");
                    sysAccount = node.getSysAccount();
                }
            }
            Map map = basShelfService.qryShelfName(sysAccount);
            return RetUtils.res(map, BasShelfAddr.class, BasShelfAddr.INCLUDES);
        } catch (CustException ce) {
            logger.error("BaseShelfController.qryShelfName.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.qryShelfName.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 查询货架号
     *
     * @param userNo
     * @param channelId
     * @param shelfAddr 货架地址
     * @param shelfNo   货架号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qryShelf")
    public String qryShelf(String userNo, String nodeId, String channelId, String shelfAddr, String shelfNo) {
        try {
//            if (!StringUtil.areNotEmpty(userNo, channelId)) {
//                return RetUtils.resFailed("参数传递错误,请核对信息!");
//            }
            String sysAccount = "";
            if (StringUtil.isNotEmpty(userNo)) {
                SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
                if (null != sysUser) {
                    //return RetUtils.resFailed("用户不存在");
                    sysAccount = sysUser.getSysAccount();
                }
            }
            if (StringUtil.isNotEmpty(nodeId)) {
                SysGroupNode node = sysGroupNodeService.selectByPrimaryKey(nodeId);
                if (null != node) {
                    //return RetUtils.resFailed("用户不存在");
                    sysAccount = node.getSysAccount();
                }
            }

            List list = basShelfService.qryShelf(sysAccount, shelfAddr, shelfNo);
            return RetUtils.res(list, BasShelfNo.class, BasShelfNo.INCLUDES1);
        } catch (CustException ce) {
            logger.error("BaseShelfController.qryShelf.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.qryShelf.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 扫描或手动输入货架号时，检测当前账套下是否存在唯一的货架号
     *
     * @param nodeId
     * @param shelfNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkShelfAddr", method = RequestMethod.POST)
    public String checkShelfAddr(String nodeId, String shelfNo) {
        try {
            if (!StringUtil.areNotEmpty(nodeId, shelfNo)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            List list = basShelfService.checkShelfAddr(nodeId, shelfNo);
            return RetUtils.res(list, BasShelfNo.class, BasShelfNo.INCLUDES1);
        } catch (CustException ce) {
            logger.error("BaseShelfController.checkShelfAddr.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.checkShelfAddr.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 修改货架
     *
     * @param userNo
     * @param channelId
     * @param type          0 修改货架地址 1 修改货架号
     * @param shelfAddrId   货架地址id 必填
     * @param shelfAddrName 货架地址 必填
     * @param shelfNoId     货架号id  type为1时必填
     * @param shelfNo       货架号 type为1时必填
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateShelf", method = RequestMethod.POST)
    public String updateShelf(String userNo, String channelId, String type, String shelfAddrId, String shelfAddrName, String shelfNoId, String shelfNo) {
        try {
            if (!StringUtil.areNotEmpty(userNo, channelId, type)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            if (StringUtil.equals(type, "0")) {
                if (!StringUtil.areNotEmpty(shelfAddrId, shelfAddrName)) {
                    return RetUtils.resFailed("参数传递错误,请核对信息!");
                }
            } else if (StringUtil.equals(type, "1")) {
                if (!StringUtil.areNotEmpty(shelfAddrId, shelfAddrName, shelfNoId, shelfNo)) {
                    return RetUtils.resFailed("参数传递错误,请核对信息!");
                }
            } else {
                return RetUtils.resFailed("无效的type值");
            }
            SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
            if (null == sysUser) {
                return RetUtils.resFailed("用户不存在");
            }
            int result = basShelfService.updateShelf(sysUser, type, shelfAddrId, shelfAddrName, shelfNoId, shelfNo);
            if (result > 0) {
                return RetUtils.resSuccess();
            }
            return RetUtils.resFailed("操作失败");
        } catch (CustException ce) {
            logger.error("BaseShelfController.updateShelf.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.updateShelf.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }

    /**
     * 删除货架
     *
     * @param userNo
     * @param channelId
     * @param type      0 删除货架地址 1 删除货架号
     * @param id        货架地址id/货架号id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShelf", method = RequestMethod.POST)
    public String deleteShelf(String userNo, String channelId, String type, String id) {
        try {
            if (!StringUtil.areNotEmpty(userNo, channelId, type, id)) {
                return RetUtils.resFailed("参数传递错误,请核对信息!");
            }
            SysUser sysUser = sysUserService.queryByUserNo(userNo, Constant.CHANNELID_CDROOM);
            if (null == sysUser) {
                return RetUtils.resFailed("用户不存在");
            }
            int result = basShelfService.deleteShelf(sysUser, type, id);
            if (result > 0) {
                return RetUtils.resSuccess();
            }
            return RetUtils.resFailed("操作失败");
        } catch (CustException ce) {
            logger.error("BaseShelfController.deleteShelf.error", ce);
            return RetUtils.resFailed(ce.getMsg());
        } catch (Exception e) {
            logger.error("BaseShelfController.deleteShelf.error", e);
            return RetUtils.resFailed("系统异常");
        }
    }
}
