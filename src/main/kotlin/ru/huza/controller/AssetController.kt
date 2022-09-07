package ru.huza.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.dto.AssetDefLink
import ru.huza.dto.AssetDto
import ru.huza.dto.toLink
import ru.huza.model.request.AssetSaveModel
import ru.huza.service.AssetDefService
import ru.huza.service.AssetService

@RestController
@RequestMapping(path = ["/assets"])
class AssetController {

    @set:Autowired
    lateinit var assetService: AssetService

    @set:Autowired
    lateinit var assetDefService: AssetDefService

    @GetMapping
    fun findAllAssetDefs(
       // @AuthenticationPrincipal authentication: UserDetails
    ): List<AssetDto> = assetService.findAll()

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createAsset(
        @RequestBody model: AssetSaveModel
       // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDto {
        return assetService.save(
            fillFromSaveModel(
                model = model,
                assetDefLink = assetDefService.findById(model.assetDefId!!).toLink()
            )
        )
    }

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
    ): AssetDto = assetService.save(
        fillFromSaveModel(
            asset = assetService.findById(id),
            model = model
        )
    )

    private fun fillFromSaveModel(
        asset: AssetDto? = null,
        model: AssetSaveModel,
        assetDefLink: AssetDefLink? = null
    ): AssetDto = AssetDto(
        id = asset?.id,
        assetDef = assetDefLink ?: asset?.assetDef ?: error("assetDef was null"),
        code = model.code ?: asset?.code,
        name = model.name ?: asset?.name,
        description = model.description ?: asset?.description,
        quantity = model.quantity ?: asset?.quantity ?: error("quantity was null")
    )
}
