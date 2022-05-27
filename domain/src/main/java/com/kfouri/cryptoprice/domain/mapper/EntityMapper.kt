package com.kfouri.cryptoprice.domain.mapper

interface EntityMapper<Model, Entity> {

    fun toModel(entity: Entity): Model
    fun toEntity(model: Model): Entity

    fun toModelList(entityList: List<Entity>): List<Model> {
        val modelList = ArrayList<Model>()

        entityList.forEach { entity: Entity ->
            val model: Model? = toModel(entity)
            if (model != null)
                modelList.add(model)
        }

        return modelList
    }

    fun toEntityList(modelList: List<Model>): List<Entity> {
        val entityList = ArrayList<Entity>()

        modelList.forEach { model: Model ->
            val entity: Entity? = toEntity(model)
            if (entity != null)
                entityList.add(entity)
        }

        return entityList
    }
}