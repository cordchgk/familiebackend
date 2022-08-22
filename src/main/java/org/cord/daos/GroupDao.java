package org.cord.daos;

import org.cord.Entities.Group;
import org.cord.Entities.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@RequestScoped
public class GroupDao extends EMCreator implements DAO, Serializable {


    UserDao userDao;

    Group group;


    public GroupDao(){
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


    public List<Group> getUserGroupsByToken(User user) {

        Query query = entityManager.createNativeQuery(
                "SELECT gruppe.gid, name, gruppe.userid FROM gruppe,users,memberofgroup WHERE gruppe.gid = memberofgroup.gid AND memberofgroup.uid = users"
                        + ".userid AND users.apitoken = ?",
                Group.class);
        query.setParameter(1,
                           user.getApitoken());
        List results = query.getResultList();
        return results;
    }


}
