package ru.huza.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@[Entity Audited]
@Table(
    name = AssetDef.TABLE_NAME,
    uniqueConstraints = [
        UniqueConstraint(
            name = "idx_${AssetDef.TABLE_NAME}__${AssetDef.CODE}",
            columnNames = [AssetDef.CODE],
        ),
    ],
)
@EntityListeners(AuditingEntityListener::class)
class AssetDef : BaseEntity {

    private var id: Long? = null

    @get:Column(name = "type", nullable = false)
    var type: String? = null

    @get:Column(name = "code", nullable = false, unique = true)
    var code: String? = null

    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:Column(name = "description")
    var description: String? = null

    @get:Column(name = "img_orig_url")
    var imgOrigUrl: String? = null

    @get:Column(name = "cost")
    var cost: String? = null

    constructor()

    constructor(entity: AssetDef): this() {
        this.id = entity.id

        this.type = entity.type
        this.code = entity.code
        this.name = entity.name
        this.description = entity.description

        this.imgOrigUrl = entity.imgOrigUrl
        this.cost = entity.cost

        this.creationDate = entity.creationDate
        this.auditDate = entity.auditDate
        this.version = entity.version
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    override fun getId(): Long? = id

    override fun setId(id: Long?) {
        this.id = id
    }

    companion object {

        const val TABLE_NAME: String = "asset_def"

        const val ID: String = "id"

        const val TYPE: String = "type"

        const val CODE: String = "code"

        const val NAME: String = "name"

        const val DESCRIPTION: String = "description"

        const val IMG_ORIG_URL: String = "imgOrigUrl"
    }
}
