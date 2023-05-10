package nusiss.csf.server.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nusiss.csf.server.model.Post;
import nusiss.csf.server.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private AmazonS3 s3Client;

    @Value("${DO_STORAGE_BUCKETNAME}")
    private String bucketName;

    // Method that notifies user if the upload is successful or not
    // Implementation is with angular and needs to be in Json
    public String postPictureWithCommentsSQL(Post post) {

        boolean isUploaded = postRepo.postPictureWithCommentsSQL(post);
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

    // Upload file into S3
    public String uploadS3(MultipartFile picture, String comment) throws IOException {

        // One or more file metadata to be associated with the object
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Ridzy");
        userData.put("uploadDateTime", LocalDateTime.now().toString());
        userData.put("originalFileName", picture.getOriginalFilename());
        userData.put("comment", comment);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(picture.getContentType());
        metadata.setContentLength(picture.getSize());
        metadata.setUserMetadata(userData);

        String objectKey = UUID.randomUUID().toString().substring(0, 8);
        System.out.println(">>>>uploadS3(): " + picture.getOriginalFilename());

        // The file extension of the uploaded file is extracted using a StringTokenizer.
        // If the file extension is "blob", it is assumed to be a PNG image, and the
        // extension is changed to "blob.png"
        StringTokenizer tk = new StringTokenizer(picture.getOriginalFilename(), ".");
        int count = 0;
        String filenameExt = "";
        while (tk.hasMoreTokens()) {
            if (count == 1) {
                filenameExt = tk.nextToken();
                break;
            } else {
                filenameExt = tk.nextToken();
                count++;
            }
        }
        if (filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png";

        PutObjectRequest putRequest = new PutObjectRequest(
            bucketName, "myobject%s.%s".formatted(objectKey, filenameExt), 
            picture.getInputStream(), metadata);
        
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        // The S3 client is used to upload the file to the S3 bucket with the putObject method, using the putRequest object created earlier
        s3Client.putObject(putRequest); 
        // returns a String representing the unique object key of the uploaded file in the S3 bucket, 
        // with the format "myobject%s.%s", where %s placeholders are replaced 
        // by the generated unique key and the file extension.
        return "myobject%s.%s".formatted(objectKey, filenameExt);

    } // end of uploadS3()
}