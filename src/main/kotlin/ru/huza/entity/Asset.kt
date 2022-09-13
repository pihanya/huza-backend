package ru.huza.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.envers.Audited

@[Entity Audited]
@Table(name = "asset")
class Asset : BaseEntity() {

    private var id: Long? = null

    @get:Column(name = "code")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    override fun getId(): Long? = id

    override fun setId(id: Long?) {
        this.id = id
    }

    companion object {

        const val CODE: String = "code"

        const val NAME: String = "name"

        const val DESCRIPTION: String = "description"

        const val QUANTITY: String = "quantity"

        const val ASSET_DEF: String = "assetDef"
    }
}
