package ru.huza.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.envers.Audited

@[Entity Audited]
@Table(
    name = Asset.TABLE_NAME,
    uniqueConstraints = [
        UniqueConstraint(
            name = "idx_${Asset.TABLE_NAME}__${Asset.CODE}",
            columnNames = [Asset.CODE],
        ),
    ],
)
class Asset : BaseEntity {

    private var id: Long? = null

    @get:Column(name = "code", unique = true)
    var code: String? = null

    @get:Column(name = "name")
    var name: String? = null

    @get:Column(name = "description")
    var description: String? = null

    @get:Column(name = "quantity", nullable = false)
    var quantity: Long? = null

    @get:ManyToOne(optional = false)
    @get:JoinColumn(name = "asset_def_id")
    var assetDef: AssetDef? = null

    constructor()

    constructor(entity: Asset) : this() {
        this.id = entity.id
        this.assetDef = entity.assetDef

        this.code = entity.code
        this.name = entity.name
        this.description = entity.description

        this.quantity = entity.quantity

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

        const val TABLE_NAME: String = "asset"

        const val ID: String = "id"

        const val ASSET_DEF: String = "assetDef"

        const val CODE: String = "code"

        const val NAME: String = "name"

        const val DESCRIPTION: String = "description"

        const val QUANTITY: String = "quantity"
    }
}
