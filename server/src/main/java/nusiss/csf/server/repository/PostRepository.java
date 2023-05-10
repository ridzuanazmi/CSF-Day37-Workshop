package nusiss.csf.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import nusiss.csf.server.model.Post;

@Repository
public class PostRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_POSTS = "insert into posts (post_id, comments, picture) values (?, ?, ?)";

    // Returns true if successful ( >0 )
    public boolean postPictureWithCommentsSQL(Post post) {
        return jdbcTemplate.update(SQL_INSERT_POSTS, 
            post.getPostId(), post.getComments(), post.getPicture()) > 0;
    }
    
    
}
