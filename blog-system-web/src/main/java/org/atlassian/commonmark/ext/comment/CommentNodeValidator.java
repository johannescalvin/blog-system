package org.atlassian.commonmark.ext.comment;


public class CommentNodeValidator {

    public boolean isValid(CommentNode commentNode){
        return isAllowedType(commentNode.getType())
                && isAllowedLang(commentNode.getSymbol())
                && isAllowedText(commentNode.getText());
    }

   protected boolean isAllowedType(String type){
        // 只处理规定格式的注释
       return true;
    }

    protected boolean isAllowedLang(String lang){

        return true;
    }

    protected boolean isAllowedText(String text){
        if (text == null || text.trim().length() == 0){
            return false;
        }
        return true;
    }
}
