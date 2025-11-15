package com.school.cooperation.common.utils;

import com.school.cooperation.common.constant.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一响应结果类
 *
 * @author Home School Team
 */
@Data
@Schema(description = "统一响应结果")
public class Result<T> {

    @Schema(description = "响应码")
    private Integer code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应时间")
    private String timestamp;

    @Schema(description = "请求ID")
    private String requestId;

    public Result() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ErrorCode.SUCCESS, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ErrorCode.SYSTEM_ERROR, message);
    }

    /**
     * 失败响应（自定义错误码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 失败响应（使用错误码常量）
     */
    public static <T> Result<T> error(Integer errorCode) {
        return new Result<>(errorCode, getDefaultMessage(errorCode));
    }

    /**
     * 参数错误响应
     */
    public static <T> Result<T> paramError(String message) {
        return new Result<>(ErrorCode.PARAM_ERROR, message);
    }

    /**
     * 未授权响应
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(ErrorCode.UNAUTHORIZED, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(ErrorCode.FORBIDDEN, message);
    }

    /**
     * 数据不存在响应
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(ErrorCode.DATA_NOT_FOUND, message);
    }

    /**
     * 业务失败响应
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ErrorCode.OPERATION_FAILED, message);
    }

    /**
     * 设置请求ID
     */
    public Result<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * 设置数据
     */
    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ErrorCode.SUCCESS == this.code;
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 获取默认错误消息
     */
    private static String getDefaultMessage(Integer errorCode) {
        return switch (errorCode) {
            case ErrorCode.PARAM_ERROR -> "参数错误";
            case ErrorCode.UNAUTHORIZED -> "未授权";
            case ErrorCode.FORBIDDEN -> "禁止访问";
            case ErrorCode.DATA_NOT_FOUND -> "数据不存在";
            case ErrorCode.OPERATION_FAILED -> "操作失败";
            case ErrorCode.AUTH_ERROR -> "认证失败";
            case ErrorCode.TOKEN_EXPIRED -> "Token已过期";
            case ErrorCode.TOKEN_INVALID -> "Token无效";
            case ErrorCode.USER_NOT_FOUND -> "用户不存在";
            case ErrorCode.PASSWORD_ERROR -> "密码错误";
            case ErrorCode.NETWORK_ERROR -> "网络错误";
            case ErrorCode.RATE_LIMIT_EXCEEDED -> "请求频率超限";
            default -> "系统错误";
        };
    }
}