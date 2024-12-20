package com.gaotianchi.auth.dao;

import com.gaotianchi.auth.dao.base.UserBaseDao;
import com.gaotianchi.auth.dao.base.UserRoleBaseDao;
import com.gaotianchi.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

/**
 * 用户表(User)表数据库访问层
 *
 * @author gaotianchi
 * @since 2024-11-27 21:35:13
 */
@Mapper
public interface UserDao extends UserBaseDao, UserRoleBaseDao {

    User selectByUsernameOrEmail(String usernameOrEmail);

    /**
     * role name with prefix ROLE_, example: {"ROLE_ADMIN", "ALL"}
     * @return set of user permission or role name
     * @author gaotianchi
     * @since 2024/11/28 21:00
     **/
    Set<String> selectUserRolesAndPermissionsNamesByUsernameOrEmail(String usernameOrEmail);

    long countByRoleCodes(@Param("roleCodes") List<Integer> roleCodes);

    List<User> selectUsersWithCertainRoles(@Param("roleCodes") List<Integer> roleCodes, @Param("pageable") PageRequest pageRequest);

    long countByPermissionCodes(@Param("permissionCodes") List<Integer> permissionCodes);

    List<User> selectUsersWithCertainPermissions(@Param("permissionCodes") List<Integer> permissionCodes, @Param("pageable") PageRequest pageRequest);
}

