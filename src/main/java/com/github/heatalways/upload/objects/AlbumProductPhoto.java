package com.github.heatalways.upload.objects;

import com.github.heatalways.objects.market.Market;
import com.github.heatalways.objects.photos.Photos;
import com.github.heatalways.VkApi;
import com.github.heatalways.jsonHandler.JsonHandler;
import com.github.heatalways.upload.BodyOfRequest;
import com.github.heatalways.upload.UploadObject;
import com.github.heatalways.utils.ArrayToString;

import java.io.File;

/**
 * Класс для загрузки фото альбома товаров.
 * @author heat"kazyxanovr1@gmail.com"
 */
public class AlbumProductPhoto extends UploadObject {
    private String group_id;

    /**
     * Конструктор, принимающий в качестве параметра объект класса VkApi
     * @param vkApi объект класса VkApi
     */
    public AlbumProductPhoto(VkApi vkApi) {
        super(vkApi);
    }

    /**
     * Загрузка фото на подборку товаров
     * @param group_id идентификатор группы
     * @param file фото
     * @return объект класса AlbumProductPhoto
     */
    public AlbumProductPhoto upload(String group_id, File file) {
        this.group_id = group_id;
        String upload_url = vkApi.photos.method(Photos.getMarketAlbumUploadServer).params(
                "group_id=" + group_id).execute().get("upload_url").toString();
        response = new JsonHandler(BodyOfRequest.getMarketAlbumPhoto(upload_url, file));
        return this;
    }

    /**
     * Сохранение результата
     * @return объект класса AlbumProductPhoto
     */
    public AlbumProductPhoto save() {
        response = vkApi.photos.method(Photos.saveMarketAlbumPhoto).params(
            "group_id=" + group_id,
                "photo=" + response.get("photo"),
                "server=" + response.get("server"),
                "hash=" + response.get("hash")
        ).execute().get(0);
        return this;
    }

    /**
     * Создание альбома товаров, используя загруженное фото
     * @param args дополнительные параметры, которые будут использоваться в методах: market.addAlbum
     * @return объект класса JsonHandler
     */
    public JsonHandler addAlbum(String... args) {
        return vkApi.market.method(Market.addAlbum).params(
            "owner_id=-" + group_id,
                "photo_id=" + response.get("id"),
                ArrayToString.toStr(args)
        ).execute();
    }

    /**
     * Редактирование альбома товаров, используя загруженное фото
     * @param args дополнительные параметры, которые будут использоваться в методах: market.editAlbum
     * @return объект класса JsonHandler
     */
    public JsonHandler editAlbum(String... args) {
        return vkApi.market.method(Market.editAlbum).params(
                "owner_id=-" + group_id,
                "photo_id=" + response.get("id"),
                ArrayToString.toStr(args)
        ).execute();
    }
}
