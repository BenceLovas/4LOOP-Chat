package Java.com.forloop;

import com.forloop.dao.UserDaoHibernate;
import com.forloop.model.User;
import com.forloop.persistence.PersistenceManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.persistence.EntityManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DaoTest {

    @Mock
    private EntityManager entityManager = mock(EntityManager.class);

    @Mock
    private User user = mock(User.class);

    private UserDaoHibernate userDaoHibernate;

    @Test
    public void insertUserTest(){
        when(user.getId()).thenReturn((long) 1);
        doNothing().when(entityManager).persist(any(User.class));
        userDaoHibernate = new UserDaoHibernate(EntityManager);
        User returnedUser = userDaoHibernate.insertUser(user);
        Assertions.assertTrue(returnedUser.getId() == user.getId());
    }
}
