package ru.huza.core.service.impl

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import ru.huza.core.config.EmbeddedDatabaseConfig
import ru.huza.core.config.TestConfig
import ru.huza.core.exception.NotFoundException
import ru.huza.core.model.dto.BuildOrderDto
import ru.huza.core.model.request.BuildOrderSaveModel
import ru.huza.core.model.request.BuildOrderSetStatusRequest
import ru.huza.core.service.BuildOrderService
import ru.huza.data.dao.AssetDao
import ru.huza.data.dao.AssetDefDao
import ru.huza.data.dao.BuildOrderDao
import ru.huza.data.entity.AssetDef
import ru.huza.data.entity.BuildOrder
import ru.huza.data.model.enum.BuildOrderStatus

@DataJpaTest(
    properties = [
        "spring.datasource.url=jdbc:postgresql://localhost:15432/huza",
        "spring.jpa.hibernate.generate-ddl=true",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "huza.api-url=http://localhost:4242/api/",
        "huza.file-storage-path=.temp/huza-files",
    ],
)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = [TestConfig::class, EmbeddedDatabaseConfig::class])
class BuildOrderServiceImplTests(
    assetDefDao: AssetDefDao,
    assetDao: AssetDao,
    buildOrderDao: BuildOrderDao,
    buildOrderService: BuildOrderService,
) : ShouldSpec({

    val now = LocalDateTime.now()

    lateinit var assetDef: AssetDef

    beforeEach {
        assetDef = assetDefDao.save(
            AssetDef().apply {
                this.type = "myType"
                this.code = "myCode"
                this.name = "myName"
                this.imgOrigUrl = "myImgOrigUrl"
            },
        )
    }

    should("create() should fill entity fields property") {

        val savedEntity = buildOrderService.create(
            BuildOrderSaveModel(
                assetDefId = assetDef.id!!,
                comment = "ASAP",
                pushFront = true,
            ),
        )

        val foundEntity = buildOrderService.findById(savedEntity.id!!)

        foundEntity shouldBe savedEntity
    }

    context("search methods") {

        should("ordinal adjustment done on findAll()") {
            val entity1 = buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            val entity2 = buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            val entity3 = buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )

            val entityList = buildOrderService.findAll()

            val entity1Dto = entityList.first { it.id == entity1.id }
            val entity2Dto = entityList.first { it.id == entity2.id }
            val entity3Dto = entityList.first { it.id == entity3.id }

            entity1Dto.ordinal shouldBe 1
            entity2Dto.ordinal shouldBe 2
            entity3Dto.ordinal shouldBe 3
        }

        should("ordinal adjustment done on findAllSorted()") {
            buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )

            val orders = buildOrderService.findAllSorted()
            val (entity1Dto, entity2Dto, entity3Dto) = orders

            entity1Dto.ordinal shouldBe 1
            entity2Dto.ordinal shouldBe 2
            entity3Dto.ordinal shouldBe 3
        }

        should("findById() returns existing entity") {
            val entity = buildOrderDao.save(
                BuildOrder().apply {
                    this.status = BuildOrderStatus.CREATED
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            shouldNotThrow<Throwable> { buildOrderService.findById(entity.id!!) }
        }

        should("findById() throws NotFoundException if entity not found") {
            shouldThrow<NotFoundException> { buildOrderService.findById(1337L) }
        }
    }

    context("setStatus() method") {

        data class SetStatusTestCase(
            val fromStatus: BuildOrderStatus,
            val toStatus: BuildOrderStatus,
        )

        assetDef = assetDefDao.save(
            AssetDef().apply {
                this.type = "myType"
                this.code = "myCode"
                this.name = "myName"
                this.imgOrigUrl = "myImgOrigUrl"
            },
        )

        fun doTestSetStatus(case: SetStatusTestCase): BuildOrderDto {
            val entity = buildOrderDao.save(
                BuildOrder().apply {
                    this.status = case.fromStatus
                    this.assetDef = assetDef
                    this.ordinal = null
                }
            )
            return buildOrderService.setStatus(
                id = entity.id!!,
                request = BuildOrderSetStatusRequest(case.toStatus)
            )
        }

        withData(
            nameFn = { (from, to) -> "idempotent transition to [$to]" },
            SetStatusTestCase(BuildOrderStatus.CREATED, BuildOrderStatus.CREATED),
            SetStatusTestCase(BuildOrderStatus.REFUSED, BuildOrderStatus.REFUSED),
            SetStatusTestCase(BuildOrderStatus.IN_PROGRESS, BuildOrderStatus.IN_PROGRESS),
            SetStatusTestCase(BuildOrderStatus.FINISHED, BuildOrderStatus.FINISHED),
        ) {  case -> doTestSetStatus(case) }

        withData(
            nameFn = { (from, to) -> "successful transition from [$from] to [$to]" },
            SetStatusTestCase(BuildOrderStatus.CREATED, BuildOrderStatus.IN_PROGRESS),
            SetStatusTestCase(BuildOrderStatus.CREATED, BuildOrderStatus.REFUSED),
            SetStatusTestCase(BuildOrderStatus.IN_PROGRESS, BuildOrderStatus.REFUSED),
            SetStatusTestCase(BuildOrderStatus.IN_PROGRESS, BuildOrderStatus.FINISHED),
        ) {  case -> doTestSetStatus(case) }

        withData(
            nameFn = { (from, to) -> "error transition from [$from] to [$to]" },
            SetStatusTestCase(BuildOrderStatus.IN_PROGRESS, BuildOrderStatus.CREATED),
            SetStatusTestCase(BuildOrderStatus.REFUSED, BuildOrderStatus.CREATED),
            SetStatusTestCase(BuildOrderStatus.REFUSED, BuildOrderStatus.IN_PROGRESS),
            SetStatusTestCase(BuildOrderStatus.REFUSED, BuildOrderStatus.FINISHED),
            SetStatusTestCase(BuildOrderStatus.FINISHED, BuildOrderStatus.IN_PROGRESS),
            SetStatusTestCase(BuildOrderStatus.FINISHED, BuildOrderStatus.REFUSED),
        ) {  case -> shouldThrow<IllegalStateException> { doTestSetStatus(case) } }
    }
})
