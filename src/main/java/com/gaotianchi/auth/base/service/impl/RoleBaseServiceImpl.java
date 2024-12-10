package com.gaotianchi.auth.base.service.impl;

import com.gaotianchi.auth.base.dao.RoleBaseDao;
import com.gaotianchi.auth.base.entity.Role;
import com.gaotianchi.auth.base.service.RoleBaseService;
import com.gaotianchi.auth.enums.Code;
import com.gaotianchi.auth.exception.SQLException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 角色表(Role)表服务实现类
 *
 * @author gaotianchi
 * @since 2024-12-07 11:29:50
 */
@Service("roleBaseService")
public class RoleBaseServiceImpl implements RoleBaseService {

    private final RoleBaseDao roleBaseDao;

    public RoleBaseServiceImpl(RoleBaseDao roleBaseDao) {
        Assert.notNull(roleBaseDao, "RoleRepository cannot be null");
        this.roleBaseDao = roleBaseDao;
    }

    @Override
    public void addNewRole(Role role) {
        if (roleBaseDao.insertRole(role) != 1) {
            throw new SQLException(Code.SQL_INSERT_ERROR);
        }
    }

    @Override
    public void addNewRolesInBatches(List<Role> roles) {
        if (roleBaseDao.insertRolesInBatches(roles) != roles.size()) {
            throw new SQLException(Code.SQL_INSERT_ERROR);
        }
    }

    @Override
    public void removeRoleById(Integer id) {
        if (roleBaseDao.deleteRoleById(id) != 1) {
            throw new SQLException(Code.SQL_DELETE_ERROR);
        }
    }

    @Override
    public void removeRolesInBatchesByIds(List<Integer> ids) {
        if (roleBaseDao.deleteRolesInBatchesByIds(ids) != ids.size()) {
            throw new SQLException(Code.SQL_DELETE_ERROR);
        }
    }

    @Override
    public void updateRoleById(Role role) {
        if (roleBaseDao.updateRoleById(role) != 1) {
            throw new SQLException(Code.SQL_UPDATE_ERROR);
        }
    }

    @Override
    public void addNewOrUpdateExistingRolesInBatches(List<Role> roles) {
        if (roleBaseDao.insertOrUpdateExistingRolesInBatches(roles) != roles.size()) {
            throw new SQLException(Code.SQL_UPDATE_ERROR);
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleBaseDao.selectRoleById(id);
    }

    @Override
    public Page<Role> getRolesByPage(Role role, PageRequest pageRequest) {
        long total = roleBaseDao.countByRole(role);
        return new PageImpl<>(roleBaseDao.selectRolesByPage(role, pageRequest), pageRequest, total);
    }


}