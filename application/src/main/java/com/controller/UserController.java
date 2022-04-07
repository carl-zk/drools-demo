package com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.domain.dto.UserDTO;
import com.domain.entity.User;
import com.domain.mapstruct.UserStructMapper;
import com.domain.service.UserService;
import com.support.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author carl
 */
@RestController
@RequestMapping("/api")
public class UserController {
    private UserStructMapper userStructMapper;
    private UserService userService;

    public UserController(UserStructMapper userStructMapper, UserService userService) {
        this.userStructMapper = userStructMapper;
        this.userService = userService;
    }

    @PostMapping("/user")
    public Result<Void> createUser(@RequestBody UserDTO userDTO) {
        User user = userStructMapper.toUser(userDTO);
        userService.save(user);
        return Result.success();
    }

    @PutMapping("/user")
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        User user = userService.getById(userDTO.getId());
        userStructMapper.updateUser(userDTO, user);
        userService.updateById(user);
        return Result.success();
    }

    @GetMapping("/user/{id}")
    public Result<UserDTO> getUser(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        return Result.success(userStructMapper.toUserDTO(user));
    }

    @GetMapping("/users")
    public Result<Page<UserDTO>> listUsers(@RequestParam(required = false, defaultValue = "1") Integer current,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        IPage<User> p = Page.of(current, size);
        userService.page(p);
        Page<UserDTO> pageDTO = PageDTO.of(p.getCurrent(), p.getSize(), p.getTotal());
        pageDTO.setRecords(userStructMapper.toUserDTOs(p.getRecords()));
        return Result.success(pageDTO);
    }
}
