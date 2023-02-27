package com.foretell.minio;


import io.micronaut.context.annotation.Property;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Singleton
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    @Property(name = "minio.bucket-name")
    private String bucketName;

    public MinioService(@Property(name = "minio.endpoint") String endpoint,
                        @Property(name = "minio.access-key") String accessKey,
                        @Property(name = "minio.secret-key") String secretKey) {
        minioClient = MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public byte[] downloadFile(String filename) throws IOException {
        try (InputStream response = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filename).build())) {
            return response.readAllBytes();
        } catch (InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | ErrorResponseException | InternalException e) {
            log.error("Error during download file", e);
            throw new IOException("Failed to download file from MinIO", e);
        }
    }

    public void uploadFile(InputStream inputStream, String filename) throws IOException {
        try {
            // Create the bucket if it does not exist
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket created: {}", bucketName);
            }

            long startMillis = System.currentTimeMillis();

            File tempFile = File.createTempFile(filename, "");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            tempFile.canWrite();
            tempFile.canRead();
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .filename(tempFile.getAbsolutePath())
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            tempFile.delete();
            log.info("upload file {} execution time {} ms", filename, System.currentTimeMillis() - startMillis);
        } catch (InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | ErrorResponseException | InternalException e) {
            log.error("Error during upload file", e);
            throw new IOException("Failed to upload file to MinIO", e);
        }
    }


}
