package com.care.service.impl;

import com.care.customlization.DbNamingStrategy;
import com.care.domain.Picture;
import com.care.domain.base.BaseModel;
import com.care.domain.enums.PictureType;
import com.care.service.PictureService;
import com.care.utils.FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nujian on 16/2/23.
 */
@Service
public class PictureServiceImpl implements PictureService {

    private static DbNamingStrategy dbNamingStrategy = new DbNamingStrategy();

    @Override
    public Picture associate(BaseModel entity, Picture p) throws Exception {
        if (entity.getId() != null) {
            p.setRefId(entity.getId());
            p.setDtype(dbNamingStrategy.classToTableName(entity.getClass().getSimpleName()));
            p.setSrc(FileUtils.createFile(p.getFile()));
            p.merge();
        }
        return p;
    }

    @Override
    public Picture associate(BaseModel entity, byte[] imgData) throws Exception {

        Picture p = new Picture();
        if (entity.getId() != null) {
            p.setRefId(entity.getId());
            p.setDtype(dbNamingStrategy.classToTableName(entity.getClass().getSimpleName()));
            p.setSrc(FileUtils.createFile(imgData));
            p.persist();
        }
        return p;
    }

    @Override
    public Picture associate(BaseModel entity, byte[] imgData, PictureType type) throws Exception {
        Picture p = new Picture();
        if (entity.getId() != null) {
            p.setRefId(entity.getId());
            p.setDtype(dbNamingStrategy.classToTableName(entity.getClass().getSimpleName()));
            p.setSrc(FileUtils.createFile(imgData));
            p.setType(type);
            p.persist();
        }
        return p;
    }

    @Override
    public void associateMulti(BaseModel entity, List<byte[]> imgData) throws Exception {
        if (!CollectionUtils.isEmpty(imgData)) {
            for (int i = 0; i < imgData.size(); i++) {
                associate(entity, imgData.get(i));
            }
        }
    }

}
