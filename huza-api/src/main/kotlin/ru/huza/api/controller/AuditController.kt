package ru.huza.api.controller

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.ByteArrayOutputStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.huza.api.HttpEndpoints
import ru.huza.api.model.request.CreateAuditReportRequest
import ru.huza.core.model.dto.AssetAuditRecord

@RestController
@RequestMapping(path = [HttpEndpoints.AUDIT])
@PreAuthorize("hasAuthority('SCOPE_admin')")
class AuditController {

    @set:Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private val csvMapper: ObjectWriter = CsvMapper.builder()
        .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
        .addModule(JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .build()
        .run {
            writerFor(AssetAuditRecord::class.java)
                .with(
                    schemaFor(AssetAuditRecord::class.java)
                        .withHeader()
                )
        }

    @PostMapping(produces = ["text/csv"])
    fun createAuditReport(
        @RequestBody request: CreateAuditReportRequest
        // @AuthenticationPrincipal authentication: UserDetails
    ): ResponseEntity<ByteArray> {
        val query = buildString(capacity = 2048) {
            """
                SELECT *
                FROM (SELECT assa.id                                                    AS "ID",
                             assa.code                                                  AS "CODE",
                             assa.name                                                  AS "NAME",
                             assa.description                                           AS "DESCRIPTION",
                             assa.quantity                                              AS "QUANTITY",
                             ass.creation_date                                          AS "CREATION_DATE",
                             TO_TIMESTAMP(ass_rev.revtstmp / 1000) AT TIME ZONE 'UTC-3' AS "AUDIT_DATE",
                             assa.rev                                                   AS "REVISION",
                             assa.asset_def_id                                          AS "ASSET_DEF_ID",
                             ad.code                                                    AS "ASSET_DEF_CODE",
                             ad.name                                                    AS "ASSET_DEF_NAME",
                             ad.type                                                    AS "ASSET_DEF_TYPE"
                      FROM asset ass
                               JOIN asset_def ad ON ad.id = ass.asset_def_id
                               JOIN asset_aud assa ON assa.id = ass.id
                               JOIN revinfo ass_rev ON assa.rev = ass_rev.rev) AS rec
            """.trimIndent().let(::appendLine)

            appendLine("WHERE (TRUE = TRUE)")
            if (request.startDate != null) {
                appendLine("AND (rec.\"AUDIT_DATE\" >= ?)")
            }
            if (request.endDate != null) {
                appendLine("AND (rec.\"AUDIT_DATE\" <= ?)")
            }
            if (request.assetDefTypes != null) {
                val wildCards = generateSequence { "?" }.take(request.assetDefTypes.size).joinToString()
                appendLine("AND (rec.\"ASSET_DEF_TYPE\" IN ($wildCards))")
            }

            appendLine("ORDER BY rec.\"ASSET_DEF_ID\", rec.\"ID\", rec.\"AUDIT_DATE\"")
        }

        val args = listOfNotNull(
            request.startDate,
            request.endDate,
            request.assetDefTypes?.toTypedArray()
        )
            .flatMap { if (it is Array<*>) it.toList() else listOf(it) }
            .toTypedArray()

        val result = jdbcTemplate.query(query, ASSET_AUDIT_RECORD_ROW_MAPPER, *args)

        val os = ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
        csvMapper.writeValues(os).writeAll(result)
        return ResponseEntity
            .status(HttpStatus.OK)
            .header("Content-Disposition", "attachment;filename=myfilename.csv")
            .body(os.toByteArray())
    }

    private companion object {

        private val ASSET_AUDIT_RECORD_ROW_MAPPER: RowMapper<AssetAuditRecord> = RowMapper { rs, _ ->
            AssetAuditRecord(
                id = rs.getLong("ID"),
                code = rs.getString("CODE"),
                name = rs.getString("NAME"),
                description = rs.getString("DESCRIPTION"),
                quantity = rs.getLong("QUANTITY"),
                creationDate = rs.getTimestamp("CREATION_DATE").toLocalDateTime(),
                auditDate = rs.getTimestamp("AUDIT_DATE").toLocalDateTime(),
                revision = rs.getLong("REVISION"),
                assetDefId = rs.getLong("ASSET_DEF_ID"),
                assetDefCode = rs.getString("ASSET_DEF_CODE"),
                assetDefName = rs.getString("ASSET_DEF_NAME"),
                assetDefType = rs.getString("ASSET_DEF_TYPE")
            )
        }
    }
}
