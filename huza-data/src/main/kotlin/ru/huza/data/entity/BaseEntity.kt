package ru.huza.data.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Transient
import jakarta.persistence.Version
import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : Serializable, Persistable<Long> {

    @get:CreatedDate
    @get:Column(name = "creation_date", updatable = false)
    var creationDate: LocalDateTime? = null

    @get:LastModifiedDate
    @get:Column(name = "audit_date")
    var auditDate: LocalDateTime? = null

    @get:Version
    @get:Column(name = "version")
    var version: Long? = null

    @Transient
    override fun isNew(): Boolean = (id == null)

    abstract fun setId(id: Long?)
}
