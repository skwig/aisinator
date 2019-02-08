package sk.skwig.aisinator.common

abstract class EntityMapper<E, D> {

    abstract fun toEntity(domainObject: D): E
    abstract fun fromEntity(entityObject: E): D

    open fun toEntity(domainObjects: List<D>): List<E> = domainObjects.map { toEntity(it) }
    open fun fromEntity(entityObjects: List<E>): List<D> = entityObjects.map { fromEntity(it) }
}
