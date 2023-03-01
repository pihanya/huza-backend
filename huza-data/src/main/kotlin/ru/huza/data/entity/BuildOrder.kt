package ru.huza.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import ru.huza.data.model.enum.BuildOrderStatus

@[Entity Audited]
@Table(name = BuildOrder.TABLE_NAME)
class BuildOrder : BaseEntity {

    private var id: Long? = null

    @get:Column(name = "status", nullable = false)
    var status: BuildOrderStatus? = null

    @get:Column(name = "ordinal")
    var ordinal: Long? = null

    @get:Column(name = "comment")
    var comment: String? = null

    @get:JoinColumn(name = "asset_def_id", nullable = false)
    @get:ManyToOne(fetch = FetchType.EAGER, optional = false)
    var assetDef: AssetDef? = null

    @get:JoinColumn(name = "created_asset_id")
    @get:OneToOne(fetch = FetchType.EAGER)
    var createdAsset: Asset? = null

    constructor() : super()

    constructor(entity: BuildOrder) : this() {
        this.id = entity.id

        this.status = entity.status
        this.ordinal = entity.ordinal
        this.comment = entity.comment
        this.assetDef = entity.assetDef
        this.createdAsset = entity.createdAsset

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

        const val TABLE_NAME: String = "build_order"

        const val ID: String = "id"

        const val ASSET_DEF: String = "assetDef"

        const val CREATED_ASSET: String = "createdAsset"

        const val STATUS: String = "status"

        const val ORDINAL: String = "ordinal"

        const val COMMENT: String = "comment"
    }
}
