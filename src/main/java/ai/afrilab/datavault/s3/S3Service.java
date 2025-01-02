package ai.afrilab.datavault.s3;

import ai.afrilab.datavault.exceptions.OperationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ;

@Service
public class S3Service {

  private final S3Client s3Client;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }


  public String putObject(String bucketName, String key, MultipartFile file) {


    String imageKey = key + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .contentLength(file.getSize())
        .contentType(file.getContentType())
        .key(imageKey)
        .acl(PUBLIC_READ)
        .build();
//    return imageKey;

    try {
      s3Client.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
      return imageKey;
    } catch (IOException e) {
      throw new OperationFailedException("Error: " + e.getLocalizedMessage());
    }
  }

  public byte[] getObject(String bucketName, String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest);

    try {
      return res.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
