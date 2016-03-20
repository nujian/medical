package com.care.customlization;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Created by nujian on 2015/5/28.
 */
public class DbNamingStrategy extends ImprovedNamingStrategy {
    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
        return addSuffix(super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName));    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String classToTableName(String className) {
        return addSuffix(super.classToTableName(className));    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
        return addSuffix(super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));    //To change body of overridden methods use File | Settings | File Templates.
    }

    private String addSuffix(final String composedTableName) {

        return composedTableName.toLowerCase() + "_";

    }
}
