package ru.huza.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.huza.dto.AssetDefDto
import ru.huza.model.request.AssetDefSaveModel
import ru.huza.service.AssetDefService

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
    fun createAssetDef(
        @RequestBody model: AssetDefSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto {
        return assetDefService.save(fillFromSaveModel(model = model))
    }

    @GetMapping(path = ["/{id}"])
    fun getAssetDefById(
        @PathVariable("id") id: Long
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto = assetDefService.findById(id)

    @PostMapping(path = ["/{id}"])
    fun updateAssetDefById(
        @PathVariable("id") id: Long,
        @RequestBody model: AssetDefSaveModel
        // @AuthenticationPrincipal authentication: UserDetails
    ): AssetDefDto = assetDefService.save(
        fillFromSaveModel(
            assetDef = assetDefService.findById(id),
            model = model
        )
    )

    private fun fillFromSaveModel(
        assetDef: AssetDefDto? = null,
        model: AssetDefSaveModel
    ): AssetDefDto = AssetDefDto(
        id = assetDef?.id,
        type = model.type ?: assetDef?.type ?: error("type was null"),
        code = model.code ?: assetDef?.code ?: error("code was null"),
        name = model.name ?: assetDef?.name ?: error("name was null"),
        description = model.description ?: assetDef?.description,
        img75Url = model.img75Url ?: assetDef?.img75Url,
        img130Url = model.img130Url ?: assetDef?.img130Url,
        img250Url = model.img250Url ?: assetDef?.img250Url,
        imgOrigUrl = model.imgOrigUrl ?: assetDef?.imgOrigUrl
    )
}
