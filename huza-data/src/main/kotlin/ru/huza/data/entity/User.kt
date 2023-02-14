package ru.huza.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = User.TABLE_NAME,
    uniqueConstraints = [
        UniqueConstraint(
            name = "idx_${User.TABLE_NAME}__${User.USERNAME}",
            columnNames = [User.USERNAME],
        ),
        UniqueConstraint(
            name = "idx_${User.TABLE_NAME}__${User.EMAIL}",
            columnNames = [User.EMAIL],
        ),
    ],
)
class User : BaseEntity() {

    private var id: Long? = null

    @Column(name = "username", nullable = false, unique = true)
    var username: String? = null

    @Column(name = "email", nullable = false, unique = true)
    var email: String? = null

    @Column(name = "password", nullable = false)
    var password: String? = null

    @Column(name = "role", nullable = false)
    var role: String? = null

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    override fun getId(): Long? = id

    override fun setId(id: Long?) {
        this.id = id
    }

    companion object {

        const val TABLE_NAME: String = "service_user"

        const val ID: String = "id"

        const val USERNAME: String = "username"

        const val EMAIL: String = "email"

        const val PASSWORD: String = "password"

        const val ROLE: String = "role"
    }
}
