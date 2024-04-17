package persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.io.IOException;

@ApplicationScoped
public class MyBatisResources {

    @Produces
    @ApplicationScoped
    @SessionFactoryProvider
    private SqlSessionFactory produceSqlSessionFactory() {
        try {
            return new SqlSessionFactoryBuilder().build(
                    Resources.getResourceAsStream("configuration.xml")
            );
        } catch (IOException e) {
            throw new RuntimeException("configuration.produceSqlSessionFactory(): ", e);
        }
    }
}
