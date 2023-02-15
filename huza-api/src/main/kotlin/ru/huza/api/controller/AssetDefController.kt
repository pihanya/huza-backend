package ru.huza.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.core.model.dto.AssetDefSaveModel
import ru.huza.core.model.dto.AssetDefDto
import ru.huza.core.model.dto.AssetDefPatchModel
import ru.huza.core.service.AssetDefService

@RestController
@RequestMapping(path = ["/asset-defs"])
class AssetDefController {

    @set:Autowired
    lateinit var assetDefService: AssetDefService

    @GetMapping
    fun findAllAssetDefs(
        // @AuthenticationPrincipal authentication: UserDetails
    ): List<AssetDefDto> = assetDefService.findAll()

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    fun createAssetDef(
        @RequestBody model: AssetDefSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto {
        return assetDefService.create(model)
    }

    @GetMapping(path = ["/{id}"])
    fun getAssetDefById(
        @PathVariable("id") id: Long
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto = assetDefService.findById(id)

    @PostMapping(path = ["/{id}"])
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    fun updateAssetDefById(
        @PathVariable("id") id: Long,
        @RequestBody model: AssetDefSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto = assetDefService.updateById(id, model)

    @PatchMapping(path = ["/{id}"])
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    fun patchAssetDefById(
        @PathVariable("id") id: Long,
        @RequestBody model: AssetDefPatchModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto = assetDefService.patchById(id = id, dto = model)
}
