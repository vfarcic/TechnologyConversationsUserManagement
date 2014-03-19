package com.technologyconversations.usermanagement;

import org.hibernate.Session;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class HibernateUtilTest {

    @Test
    public void getSessionFactoryShouldNotBeNull() {
        assertThat(HibernateUtil.getSessionFactory().openSession(), notNullValue());
    }

    @Test
    public void getSessionFactoryShouldBeAbleToOpenAndCloseSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        assertThat(session.isConnected(), is(true));
        session.close();
        assertThat(session.isConnected(), is(false));
    }

}
