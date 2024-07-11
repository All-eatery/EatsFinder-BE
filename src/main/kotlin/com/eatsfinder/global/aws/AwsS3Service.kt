package com.eatsfinder.global.aws

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.eatsfinder.global.exception.profile.NotUploadImageException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class AwsS3Service(
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.url}") private val url: String
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Throws(IOException::class)
    fun uploadImage(multipartFile: MultipartFile?): String {
        val fileName = UUID.randomUUID().toString() + "-" + multipartFile?.originalFilename
        val objectMetadata = ObjectMetadata().apply {
            contentLength = multipartFile!!.size
            contentType = multipartFile.contentType
        }

        try {
            multipartFile?.inputStream.use { inputStream ->
                amazonS3Client.putObject(
                    PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
//                            .withCannedAcl(CannedAccessControlList.PublicRead)
                )
            }
        } catch (e: IOException) {
            throw NotUploadImageException("이미지 업로드에 실패했습니다.")
        }

        return "$url/$fileName"
    }

    fun deleteImage(fileName: String?) {
        if (fileName.isNullOrEmpty()) {
            throw NotUploadImageException("파일 이름이 잘못되었습니다.")
        }
        val replacedFile = fileName.replace("$url/", "")
        amazonS3Client.deleteObject(DeleteObjectRequest(bucket, replacedFile))
    }
}