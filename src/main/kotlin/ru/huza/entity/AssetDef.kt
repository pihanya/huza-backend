package ru.huza.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@[Entity Audited]
@Table(name = "asset_def")
@EntityListeners(AuditingEntityListener::class)
class AssetDef : BaseEntity() {

    private var id: Long? = null

    @get:Column(name = "type", nullable = false)
    var type: String? = null

    @get:Column(name = "code", nullable = false, unique = true)
    var code: String? = null

    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:Column(name = "description")
    var description: String? = null

    @get:Column(name = "img75_url")
    var img75Url: String? = null

    @get:Column(name = "img130_url")
    var img130Url: String? = null

    @get:Column(name = "img250_url")
    var img250Url: String? = null

    @get:Column(name = "img_orig_url")
    var imgOrigUrl: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    override fun getId(): Long? = id

    override fun setId(id: Long?) {
        this.id = id
    }

    companion object {

        const val TYPE: String = "type"
        const val CODE: String = "code"
        const val NAME: String = "name"
        const val DESCRIPTION: String = "description"
        const val IMG_75_URL: String = "img75Url"
        const val IMG_130_URL: String = "img130Url"
        const val IMG_250_URL: String = "img250Url"
        const val IMG_ORIG_URL: String = "imgOrigUrl"
    }
}
