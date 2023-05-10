package nusiss.csf.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nusiss.csf.server.model.Post;
import nusiss.csf.server.repository.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepo;

    // Method that notifies user if the upload is successful or not
    // Implementation is with angular and needs to be in Json
    public String postPictureWithComments(Post post) {

        boolean isUploaded = postRepo.postPictureWithComments(post);
        String response = "";

        if (isUploaded) {
            JsonObject joInsertPostResult = Json.createObjectBuilder()
                .add("message", "upload was successful")
                .build();
            
            response = joInsertPostResult.toString();
            return response;

        } else {
            JsonObject joInsertPostResult = Json.createObjectBuilder()
                .add("message", "upload was unsuccessful")
                .build();
            
            response = joInsertPostResult.toString();
            return response;
        }
    }
    
}
