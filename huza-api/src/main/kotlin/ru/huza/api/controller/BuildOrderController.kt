package ru.huza.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.request.BuildOrderCreateRequest
import ru.huza.core.model.request.BuildOrderSetStatusRequest
import ru.huza.core.service.BuildOrderService

@RestController
@RequestMapping(path = ["/build-orders"])
@PreAuthorize("hasAuthority('SCOPE_owner') or hasAuthority('SCOPE_builder')")
class BuildOrderController {

    @set:Autowired
    lateinit var buildOrderService: BuildOrderService

    @PostMapping("/create-new")
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createBuildOrder(
        @RequestBody request: BuildOrderCreateRequest
        // @AuthenticationPrincipal authentication: UserDetails
    ): BuildOrderDto = buildOrderService.createNew(request)

    @GetMapping
    fun findAllBuildOrders(
        // @AuthenticationPrincipal authentication: UserDetails
    ): List<BuildOrderDto> = buildOrderService.findAllSorted()

    @GetMapping(path = ["/{id}"])
    fun getBuildOrderById(
        @PathVariable("id") id: Long
        // @AuthenticationPrincipal authentication: UserDetails
    ): BuildOrderDto = buildOrderService.findById(id)

    @PostMapping(path = ["/{id}/set-status"])
    fun setStatusByBuildOrderId(
        @PathVariable("id") id: Long,
        @RequestBody request: BuildOrderSetStatusRequest
        // @AuthenticationPrincipal authentication: UserDetails
    ): BuildOrderDto = buildOrderService.setStatus(id, request)
}
