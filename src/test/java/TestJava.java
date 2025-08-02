
import Applications.model.Passport;
import Applications.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class TestJava{
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(User.class)
                .addAnnotatedClass(Passport.class).buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        try{
            session.beginTransaction();

Passport passport = session.find(Passport.class, 1);
            session.remove(passport);
            session.getTransaction().commit();


        }finally {
            session.close();
            sessionFactory.close();
        }

    }
}