package ru.huza.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import ru.huza.model.BuildOrderStatus

@[Entity Audited]
@Table(name = "build_order")
class BuildOrder : BaseEntity() {

    private var id: Long? = null

    @Column(name = "status", nullable = false)
    var status: BuildOrderStatus? = null

    @Column(name = "ordinal")
    var ordinal: Long? = null

    @Column(name = "comment")
    var comment: String? = null

    @JoinColumn(name = "asset_def_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    var assetDef: AssetDef? = null

    @JoinColumn(name = "created_asset_id")
    @OneToOne(fetch = FetchType.EAGER)
    var createdAsset: Asset? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    override fun getId(): Long? = id

    override fun setId(id: Long?) {
        this.id = id
    }
}
