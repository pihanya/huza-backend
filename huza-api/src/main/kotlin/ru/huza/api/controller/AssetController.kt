package ru.huza.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
import ru.huza.core.model.dto.AssetPatchModel
import ru.huza.core.model.dto.AssetSaveModel
import ru.huza.core.service.AssetService

@RestController
@RequestMapping(path = ["/assets"])
class AssetController {

    @set:Autowired
    lateinit var assetService: AssetService

    @GetMapping
    fun findAllAssets(
       // @AuthenticationPrincipal authentication: UserDetails
    ): List<AssetDto> = assetService.findAll()

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createAsset(
        @RequestBody model: AssetSaveModel
       // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDto =
        assetService.create(model)

    @GetMapping(path = ["/{id}"])
    fun getAssetById(
        @PathVariable("id") id: Long
       // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDto = assetService.findById(id)

    @PostMapping(path = ["/{id}"])
    fun updateAssetById(
        @PathVariable("id") id: Long,
        @RequestBody model: AssetSaveModel
       // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDto = assetService.updateById(id = id, model = model)

    @PatchMapping(path = ["{id}"])
    fun patchAssetById(
        @PathVariable("id") id: Long,
        @RequestBody model: AssetPatchModel,
    ): AssetDto = assetService.patchById(id = id, model = model)

    @DeleteMapping(path = ["/{id}"])
    fun deleteAssetById(
        @PathVariable("id") id: Long,
//        @AuthenticationPrincipal authentication: UserDetails,
    ) {
        assetService.removeById(id)
    }
}
