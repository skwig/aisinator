package sk.skwig.aisinator.feature

abstract class EntityMapper<Entity, Domain> {

    abstract fun toEntity(domain: Domain): Entity
    open fun toEntityList(domains: List<Domain>): List<Entity> = domains.map { toEntity(it) }

}

abstract class SimpleEntityMapper<Entity, Domain> : EntityMapper<Entity, Domain>() {

    abstract fun fromEntity(entity: Entity): Domain
    open fun fromEntityList(entities: List<Entity>): List<Domain> = entities.map { fromEntity(it) }

}

abstract class RelationEntityMapper<Entity, Relation, Domain> : EntityMapper<Entity, Domain>() {

    abstract fun toRelationList(domains: List<Domain>): List<Relation>
    abstract fun fromRelationList(relations: List<Relation>): List<Domain>

}
