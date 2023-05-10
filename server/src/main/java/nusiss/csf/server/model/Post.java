package nusiss.csf.server.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/*
 * In Spring Boot, the best data type to use for storing uploaded images into a SQL database would be a byte array or a BLOB (Binary Large Object) data type.
 * When receiving an uploaded image from Angular, Spring Boot can convert the image to a byte array using a MultipartFile object. 
 * This byte array can then be stored in a BLOB field in the SQL database.
 */

public class Post implements Serializable {
    
    private String postId;
    private String comments;
    private byte[] picture;

    // Getters and Setters
    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public byte[] getPicture() {
        return picture;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Post(String comments, byte[] picture) {
        this.postId = UUID.randomUUID().toString()
            .substring(0, 8); // Create unique UUID when a picture is uploaded
        this.comments = comments;
        this.picture = picture;
    }

    public Post() {
    }
    
    public static Post populate(ResultSet rs) throws SQLException {
        final Post p = new Post();
        p.setPostId(rs.getString("post_id"));
        p.setComments(rs.getString("comments"));
        p.setPicture(rs.getBytes("picture"));
        return p;
    }
    
    @Override
    public String toString() {
        return "Post [postId=" + postId + ", comments=" + comments + "]";
    }

    
}
