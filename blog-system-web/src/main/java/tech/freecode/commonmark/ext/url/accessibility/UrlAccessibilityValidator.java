package tech.freecode.commonmark.ext.url.accessibility;

public interface UrlAccessibilityValidator {
    class ValidationResult {
        public enum Status {
            OK,
            FAIL,
            IGNORE
        }

        Status status;
        String msg;

        public Status getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }



    ValidationResult validate(Url url);
}
