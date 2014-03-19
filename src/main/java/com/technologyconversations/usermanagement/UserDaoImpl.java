package com.technologyconversations.usermanagement;

import org.hibernate.*;

import java.util.List;

public class UserDaoImpl implements UserDao {

    public void putUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.saveOrUpdate(user);
        session.flush();
        session.close();
    }

    public void deleteAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.createQuery("delete from User").executeUpdate();
        session.close();
    }

    public void deleteUser(String userName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = getUser(userName);
        session.delete(user);
        session.flush();
        session.close();
    }

    public User getUser(String userName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.get(User.class, userName);
        session.close();
        return user;
    }

    public List<User> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<User> users = session.createQuery("from User").list();
        session.close();
        return users;
    }

}
