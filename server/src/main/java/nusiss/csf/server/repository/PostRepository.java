package nusiss.csf.server.repository;

import java.sql.ResultSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import nusiss.csf.server.model.Post;

@Repository
public class PostRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_POSTS = "insert into posts (post_id, comments, picture) values (?, ?, ?)";
    private static final String SQL_GET_POSTS_BYID = "SELECT * from posts where post_id = ?";

    // Returns true if successful ( >0 )
    public boolean postPictureWithCommentsSQL(Post post) {
        return jdbcTemplate.update(SQL_INSERT_POSTS, 
            post.getPostId(), post.getComments(), post.getPicture()) > 0;
    }
    
    // Read the posts table where post_id = ?
    public Optional<Post> getPostsByIdSQL(String postId) {
        
        try {
            Post post = jdbcTemplate.queryForObject(SQL_GET_POSTS_BYID,
                (ResultSet rs, int rowNum) -> {
                    final Post p = Post.populate(rs);
                    return p;
                }, postId);
            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
