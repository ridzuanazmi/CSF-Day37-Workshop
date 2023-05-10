package nusiss.csf.server.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nusiss.csf.server.model.Post;
import nusiss.csf.server.service.PostService;

@RestController
@RequestMapping(path = "/api")
public class PostController {

    @Autowired
    private PostService postSrvc;

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    // POST /api/post
    // Content-Type: multipart/form-data
    // Accept: application/json
    // Returns a message
    @PostMapping(path = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postPictureWithComments(@RequestPart String comments,
            @RequestPart MultipartFile picture) throws IOException {

        try {

            Post post = new Post(comments, picture.getBytes());
            String key = postSrvc.uploadS3(picture, comments);
            String response = postSrvc.postPictureWithCommentsSQL(post);
            System.out.printf("\n>>>S3 key: %s\n", key);
            return ResponseEntity.status(201)
                    .body(response);

        } catch (Exception e) {

            return ResponseEntity.status(500)
                    .body("Error uploading image");

        }
    }

    // Display the image directly in the browser, you can change the response to 
    // return the image as a binary stream with the correct content type.
    @GetMapping(path = "/getfile/{postId}")
    public ResponseEntity<byte[]> getFileSQL(@PathVariable String postId) {
        Optional<Post> postOpt = this.postSrvc.getPostsById(postId);

        if (!postOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Post post = postOpt.get();
        byte[] pictureBytes = post.getPicture();
        HttpHeaders headers = new HttpHeaders();

        // Set the content type to display the image correctly in the browser
        headers.setContentType(MediaType.IMAGE_JPEG); // Replace with the actual content type of the image
        headers.setContentLength(pictureBytes.length);

        return new ResponseEntity<>(pictureBytes, headers, HttpStatus.OK);
    }
    
    // This will only work if we use a Base64-encoded image in your frontend,
    // create an <img> element and set its src attribute to the Base64-encoded
    // string
    // in your frontend, create an <img> element and set its src like this: <img
    // id="image"/>
    // @GetMapping(path = "/getfile/{postId}")
    // public ResponseEntity<String> getFileSQL(@PathVariable String postId) {

    // Optional<Post> postOpt = this.postSrvc.getPostsByIdSQL(postId);
    // if (!postOpt.isPresent()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
    // }
    // Post post = postOpt.get();
    // String encodedString = Base64.getEncoder().encodeToString(post.getPicture());

    // JsonObject payload = Json.createObjectBuilder()
    // .add("picture", BASE64_PREFIX + encodedString)
    // .build();

    // return ResponseEntity.ok(payload.toString());
    // }
}
