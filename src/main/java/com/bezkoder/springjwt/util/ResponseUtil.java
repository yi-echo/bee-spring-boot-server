package com.bezkoder.springjwt.util;

import com.bezkoder.springjwt.payload.response.ApiResponse;
import com.bezkoder.springjwt.payload.response.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 响应工具类
 */
public class ResponseUtil {
    
    // 成功响应
    public static <T> ResponseEntity<ApiResponse<T>> success() {
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
    
    // 失败响应
    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> error(Integer code, String message) {
        return ResponseEntity.status(code)
                .body(ApiResponse.error(code, message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> error(Integer code, String message, T data) {
        return ResponseEntity.status(code)
                .body(ApiResponse.error(code, message, data));
    }
    
    // 参数错误
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(message));
    }
    
    // 未授权
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.unauthorized(message));
    }
    
    // 禁止访问
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.forbidden(message));
    }
    
    // 资源未找到
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(message));
    }
    
    // 方法不允许
    public static <T> ResponseEntity<ApiResponse<T>> methodNotAllowed(String message) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.methodNotAllowed(message));
    }
    
    // 冲突
    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.conflict(message));
    }
    
    // 服务器内部错误
    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.internalServerError(message));
    }
    
    // 分页响应
    public static <T> ResponseEntity<ApiResponse<PageResponse<T>>> successPage(
            List<T> content, Integer page, Integer size, Long totalElements) {
        PageResponse<T> pageResponse = new PageResponse<>(content, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success("查询成功", pageResponse));
    }
    
    public static <T> ResponseEntity<ApiResponse<PageResponse<T>>> successPage(
            String message, List<T> content, Integer page, Integer size, Long totalElements) {
        PageResponse<T> pageResponse = new PageResponse<>(content, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(message, pageResponse));
    }
}
