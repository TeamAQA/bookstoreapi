package pl.akademiaqa.bos.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;

public class FileValidator {

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg"
    );

    private static final long MAX_FILE_SIZE = 200 * 1024; // 200 KB

    public static void validateFile(MultipartFile file) {

//        System.out.println("DEBUG FILE UPLOAD:");
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getContentType());
//        System.out.println(file.getSize());
//        System.out.println(file.getName());

        if (file == null || file.isEmpty()) {
            throw new ValidationException("file incorrect input data");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ValidationException("file incorrect input data");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("file incorrect input data");
        }
    }
}
