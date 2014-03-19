package com.technologyconversations.usermanagement;

import org.hibernate.*;

import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        System.out.println(userDao.getAllUsers().size());
        User user = new User();
        user.setFullName("Viktor Farcic");
        user.setPassword("trustno1");
        user.setUpdated(new Date());
        user.setUserName("vfarcic");
        userDao.putUser(user);
        System.out.println(userDao.getAllUsers().size());
    }

    public void putUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<User> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from User");
        @SuppressWarnings("unchecked")
        List<User> users = query.list();
        session.close();
        return users;
    }

    public User getUser(String userName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.get(User.class, userName);
        session.close();
        return user;
    }

}
