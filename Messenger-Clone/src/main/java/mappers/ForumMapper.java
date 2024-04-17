package mappers;


import entities.Forum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisResources;

import java.util.List;
import java.util.Set;

@Mapper
@RequestScoped
public class ForumMapper {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    public List<Forum> getAllForums(){
        SqlSession session = sqlSessionFactory.openSession();

        List<Forum> forums = session.selectList("getAllForums");
        session.commit();
        session.close();

        return forums;
    }
    public void insertForum(Forum forum)
    {
        SqlSession session = sqlSessionFactory.openSession();

        session.insert("mappers.ForumMapper.insertForum", forum);
        session.commit();
        session.close();
    }
}
