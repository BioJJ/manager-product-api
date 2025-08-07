package biojj.managerproductapi.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardError {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<FieldMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }

    public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }
}
