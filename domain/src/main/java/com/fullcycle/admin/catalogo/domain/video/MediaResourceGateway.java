package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, Resource aResource);

    ImageMedia storeImage(VideoID anId, Resource aResource);

    void clearResources(VideoID anId);
}
