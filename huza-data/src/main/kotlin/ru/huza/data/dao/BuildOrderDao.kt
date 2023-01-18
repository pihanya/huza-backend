package ru.huza.data.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.data.entity.BuildOrder

interface BuildOrderDao : CrudRepository<BuildOrder, Long>
