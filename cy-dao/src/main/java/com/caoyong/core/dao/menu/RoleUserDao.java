package com.caoyong.core.dao.menu;

import com.caoyong.core.bean.menu.RoleUser;
import com.caoyong.core.bean.menu.RoleUserQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleUserDao {
    int countByExample(RoleUserQuery example);

    int deleteByExample(RoleUserQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleUser record);

    int insertSelective(RoleUser record);

    List<RoleUser> selectByExample(RoleUserQuery example);

    RoleUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleUser record, @Param("example") RoleUserQuery example);

    int updateByExample(@Param("record") RoleUser record, @Param("example") RoleUserQuery example);

    int updateByPrimaryKeySelective(RoleUser record);

    int updateByPrimaryKey(RoleUser record);
}