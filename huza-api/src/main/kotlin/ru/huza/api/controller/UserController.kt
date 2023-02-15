package ru.huza.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.core.model.dto.AssetDto
import ru.huza.core.model.dto.UserDto
import ru.huza.core.model.dto.UserPatchModel
import ru.huza.core.model.dto.UserSaveModel
import ru.huza.core.service.UserService

@RestController
@RequestMapping(path = ["/users"])
class UserController {

    @set:Autowired
    lateinit var userService: UserService

    @GetMapping
    fun findAllUsers(
        @AuthenticationPrincipal authentication: Jwt
    ): List<UserDto> = userService.findAll()

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createUser(
        @RequestBody model: UserSaveModel,
//        @AuthenticationPrincipal authentication: UserDetails
    ): UserDto {
        return userService.create(model = model)
    }

    @GetMapping(path = ["/{id}"])
    fun getUserById(
        @PathVariable("id") id: Long,
//        @AuthenticationPrincipal authentication: UserDetails
    ): UserDto = userService.findById(id)

    @PostMapping(path = ["/{id}"])
    fun updateUserById(
        @PathVariable("id") id: Long,
        @RequestBody model: UserSaveModel,
//        @AuthenticationPrincipal authentication: UserDetails
    ): UserDto = userService.updateById(id, model)

    @PatchMapping(path = ["{id}"])
    fun patchUserById(
        @PathVariable("id") id: Long,
        @RequestBody model: UserPatchModel,
    ): UserDto = userService.patchById(id = id, model = model)

    @DeleteMapping(path = ["/{id}"])
    fun deleteUserById(
        @PathVariable("id") id: Long,
//        @AuthenticationPrincipal authentication: UserDetails,
    ) {
        userService.removeById(id)
    }
}
