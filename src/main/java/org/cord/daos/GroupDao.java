package org.cord.daos;

import org.cord.Entities.GroupEntity;
import org.cord.Entities.UserEntity;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@RequestScoped
public class GroupDao extends EMCreator implements DAO, Serializable {


    UserDao userDao;

    GroupEntity group;


    public GroupDao() {

        this.userDao = new UserDao();
        this.createManager();
    }


    @PreDestroy
    public void destroy() {

        this.close();
    }


    @Override
    public Object deleteById(Object obj) {

        return null;
    }


    public List<GroupEntity> getUserGroupsByToken(UserEntity user) {

        Query query = entityManager.createNativeQuery(
                "SELECT gruppe.gid, name, gruppe.userid FROM gruppe,users,memberofgroup WHERE gruppe.gid = memberofgroup.gid AND memberofgroup.uid = users"
                        + ".userid AND users.apitoken = ?",
                GroupEntity.class);
        query.setParameter(
                1,
                user.getApitoken());

        return query.getResultList();
    }


    public boolean isUserMember(
            UserEntity user,
            GroupEntity group) {

        Query query = entityManager.createNativeQuery("SELECT count(m) FROM memberofgroup m WHERE uid = ? AND gid = ?");
        query.setParameter(
                1,
                user.getId());
        query.setParameter(
                2,
                group.getId());

        return (Long) query.getSingleResult() > 0L;
    }


    public List<UserEntity> getGroupUsers(GroupEntity group) {

        Query query = this.entityManager.createNativeQuery(
                "SELECT users.userid,users.firstname,users.surname,users.address,users.birthday FROM users,gruppe,memberofgroup WHERE users.userid = "
                        + "memberofgroup"
                        + ".uid AND "
                        + "memberofgroup.gid = "
                        + "gruppe.gid AND gruppe.gid = ?",
                UserEntity.class);
        query.setParameter(
                1,
                group.getId());
        List<UserEntity> toReturn = query.getResultList();

        return toReturn;
    }


    public boolean addUserToGroup(
            UserEntity toAdd,
            GroupEntity group) {

        Query query = this.entityManager.createNativeQuery("INSERT INTO memberofgroup (gid, uid) VALUES (?,?) ON CONFLICT ON CONSTRAINT memberofgroup_pkey "
                                                                   + "DO UPDATE SET "
                                                                   + "gid =?,uid=? "
                                                                   + "WHERE "
                                                                   + "memberofgroup.gid =? AND memberofgroup.uid = ?");

        query.setParameter(
                1,
                group.getId());
        query.setParameter(
                2,
                toAdd.getId());
        query.setParameter(
                3,
                group.getId());
        query.setParameter(
                4,
                toAdd.getId());
        query.setParameter(
                5,
                group.getId());
        query.setParameter(
                6,
                toAdd.getId());
        this.entityManager.getTransaction()
                          .begin();
        query.executeUpdate();
        this.entityManager.getTransaction()
                          .commit();

        return true;
    }


}
