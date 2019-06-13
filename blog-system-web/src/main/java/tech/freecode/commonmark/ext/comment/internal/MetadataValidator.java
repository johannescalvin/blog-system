package tech.freecode.commonmark.ext.comment.internal;

import tech.freecode.commonmark.ext.comment.CommentNodeValidator;

public class MetadataValidator extends CommentNodeValidator {
    @Override
    protected boolean isAllowedType(String type) {
        String[] allowed_comment_types = new String[]{"keywords","description","author","title","catagory"};
        boolean is_allowed_type = false;
        for (String allowed_type : allowed_comment_types){
            if (allowed_type.equals(type)){
                is_allowed_type = true;
            }
        }
        return is_allowed_type;

    }
}
