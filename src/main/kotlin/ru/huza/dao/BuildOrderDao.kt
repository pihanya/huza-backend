package ru.huza.dao

import org.springframework.data.repository.CrudRepository
import ru.huza.entity.BuildOrder

interface BuildOrderDao : CrudRepository<BuildOrder, Long>
