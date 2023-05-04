package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_USERS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(50), " +
            "lastName VARCHAR(50), " +
            "age TINYINT)";

    private static final String DROP_USERS_TABLE_QUERY = "DROP TABLE IF EXISTS users";
    private static final String GET_ALL_USERS_QUERY = "FROM User";
    private static final String CLEAN_USERS_TABLE_QUERY = "DELETE FROM User";

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(CREATE_USERS_TABLE_QUERY).executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(DROP_USERS_TABLE_QUERY).executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery(GET_ALL_USERS_QUERY, User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery(CLEAN_USERS_TABLE_QUERY).executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}