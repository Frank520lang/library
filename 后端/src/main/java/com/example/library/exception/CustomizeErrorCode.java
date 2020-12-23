package com.example.library.exception;

/**
 * Created by codedrinker on 2019/5/28.
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

	
	ACCOUNTID_NOT_UNION(2001, "登录ID被占用，请重新输入！"),
    ACCOUNTID_OR_PASSWORD_NOT_TRUE(2002, "账号或密码错误！！"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    NO_ACCOUNTID(2005, "用户ID错误！！"),
    NOT_LOGOUT(2006, "该用户还没还书！不能注销！！！"),
    BOOKID_NOT_UNION(2007, "该bookId已存在"),
    ACCOUNTID_NOT_USE(2008, "您来太迟了，该账户长时间未激活，已过期"),
    EMAIL_OR_ACCOUNTID_ERROR(2009, "账户或邮箱输入错误！！"),
    NO_BOOK(2010, "您手速太慢了，这本书已经被借空了！！"),
    NO_EMAIL(2011, "邮箱输入错误，找不到该邮箱用户！！"),
    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
