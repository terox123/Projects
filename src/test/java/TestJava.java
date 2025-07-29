import Applications.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestJava{
    public static void main(String[] args) {
        Configuration configuration= new Configuration().addAnnotatedClass(User.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        try{
            session.beginTransaction();

session.persist(new User());

            session.getTransaction().commit();

        }finally {
            session.close();
            sessionFactory.close();
        }
    }
}