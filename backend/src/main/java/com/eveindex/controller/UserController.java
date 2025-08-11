package com.eveindex.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户管理Controller，用于测试Knife4j文档生成
 */
@Tag(name = "用户管理", description = "用户的CRUD操作接口")
@RestController
@RequestMapping("/users")
public class UserController {

    // 模拟数据库存储
    private final ConcurrentMap<Integer, User> userMap = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    /**
     * 获取所有用户
     */
    @Operation(summary = "获取所有用户", description = "返回系统中所有用户的列表")
    @ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    /**
     * 根据ID获取用户
     */
    @Operation(summary = "根据ID获取用户", description = "通过用户ID查询用户详情")
    @Parameters({
            @Parameter(name = "id", description = "用户ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userMap.get(id);
    }

    /**
     * 创建新用户
     */
    @Operation(summary = "创建新用户", description = "添加新用户到系统中")
    @ApiResponse(responseCode = "200", description = "创建成功",
            content = @Content(schema = @Schema(implementation = User.class)))
    @PostMapping
    public User createUser(
            @Parameter(description = "用户信息", required = true)
            @RequestBody User user) {
        int id = idGenerator.getAndIncrement();
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息", description = "根据ID更新用户的信息")
    @Parameters({
            @Parameter(name = "id", description = "用户ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Integer id,
            @Parameter(description = "更新后的用户信息", required = true)
            @RequestBody User user) {
        if (!userMap.containsKey(id)) {
            return null;
        }
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @Parameters({
            @Parameter(name = "id", description = "用户ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Integer id) {
        return userMap.remove(id) != null;
    }

    /**
     * 用户实体类
     */
    @Schema(description = "用户信息实体")
    public static class User {
        @Schema(description = "用户ID", example = "1")
        private Integer id;

        @Schema(description = "用户名", example = "zhangsan")
        private String username;

        @Schema(description = "用户年龄", example = "25")
        private Integer age;

        @Schema(description = "用户邮箱", example = "zhangsan@example.com")
        private String email;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
