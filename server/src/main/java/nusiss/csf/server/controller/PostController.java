package nusiss.csf.server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
}
