package com.cdkj.cdroom.model.dao;

import com.cdkj.base.model.BaseDao;
import com.cdkj.cdroom.model.pojo.AlpBankCard;
import org.springframework.stereotype.Component;

@Component
public interface AlpBankCardMapper extends BaseDao {
    int deleteByPrimaryKey(String id);

    int insert(AlpBankCard record);

    int insertSelective(AlpBankCard record);

    AlpBankCard selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AlpBankCard record);

    int updateByPrimaryKey(AlpBankCard record);

    AlpBankCard findUserBankCard(String id);
}
