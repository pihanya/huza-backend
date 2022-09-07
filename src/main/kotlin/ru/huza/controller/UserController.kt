package ru.huza.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.dto.UserDto
import ru.huza.model.request.UserSaveModel
import ru.huza.service.UserService

@CrossOrigin
@RestController
@RequestMapping(path = ["/users"])
class UserController {

    @set:Autowired
    lateinit var userService: UserService

    @GetMapping
    fun findAllUsers(
        // @AuthenticationPrincipal authentication: UserDetails
    ): List<UserDto> = userService.findAll()

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createUser(
        @RequestBody model: UserSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): UserDto {
        return userService.save(fillFromSaveModel(model = model))
    }

    @GetMapping(path = ["/{id}"])
    fun getUserById(
        @PathVariable("id") id: Long
        // @AuthenticationPrincipal authentication: UserDetails
    ): UserDto = userService.findById(id)

    @PostMapping(path = ["/{id}"])
    fun updateUserById(
        @PathVariable("id") id: Long,
        @RequestBody model: UserSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): UserDto = userService.save(
        fillFromSaveModel(
            user = userService.findById(id),
            model = model
        )
    )

    @DeleteMapping(path = ["/{id}"])
    fun deleteUserById(
        @PathVariable("id") id: Long,
        // @AuthenticationPrincipal authentication: UserDetails
    ) {
        userService.removeById(id)
    }

    private fun fillFromSaveModel(
        user: UserDto? = null,
        model: UserSaveModel
    ): UserDto = UserDto().apply {
        this.id = user?.id
        this.email = model.email ?: user?.email ?: error("email was null")
        this.role = model.role ?: user?.role ?: error("role was null")
        setUsername(user?.username ?: this.email!!)
        setPassword(model.password ?: user?.password ?: error("password was null"))
    }
}
