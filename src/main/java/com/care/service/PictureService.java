package com.care.service;

import com.care.domain.Picture;
import com.care.domain.base.BaseModel;
import com.care.domain.enums.PictureType;

import java.util.List;

/**
 * Created by nujian on 16/2/23.
 */
public interface PictureService {

    Picture associate(BaseModel entity, Picture p) throws Exception;

    Picture associate(BaseModel entity, byte[] imgData) throws Exception;

    Picture associate(BaseModel entity, byte[] imgData, PictureType type) throws Exception;

    void associateMulti(BaseModel entity, List<byte[]> imgData) throws Exception;
}
