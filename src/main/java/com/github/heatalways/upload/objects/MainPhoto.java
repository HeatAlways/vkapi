package com.github.heatalways.upload.objects;

import com.github.heatalways.objects.photos.Photos;
import com.github.heatalways.VkApi;
import com.github.heatalways.jsonHandler.JsonHandler;
import com.github.heatalways.upload.BodyOfRequest;
import com.github.heatalways.upload.UploadObject;

import java.io.File;

/**
 * Класс для загрузки основного фото.
 * @author heat"kazyxanovr1@gmail.com"
 */
public class MainPhoto extends UploadObject {

    /**
     * Конструктор, принимающий в качестве параметра объект класса VkApi
     * @param vkApi объект класса VkApi
     */
    public MainPhoto(VkApi vkApi) {
        super(vkApi);
    }

    /**
     * Загрузка основной фотографии пользователя
     * @param file фото
     * @return объект класса MainPhoto
     * @see MainPhoto#upload(String, File)
     * @see MainPhoto#upload(File, String)
     * @see MainPhoto#upload(String, File, String)
     */
    public MainPhoto upload(File file) {
        return upload(file, "");
    }

    /**
     * Загрузка основной фотографии пользователя
     * @param file фото
     * @param square_crop квадратная миниатюра фото в формате "x,y,w" (без кавычек), где x и y — координаты верхнего правого угла миниатюры, а w — сторона квадрата
     * @return объект класса MainPhoto
     * @see MainPhoto#upload(String, File)
     * @see MainPhoto#upload(File)
     * @see MainPhoto#upload(String, File, String)
     */
    public MainPhoto upload(File file, String square_crop) {
        return upload("",file, square_crop);
    }

    /**
     * Загрузка основной фотографии группы
     * @param owner_id идентификатор сообщества или текущего пользователя
     * @param file фото
     * @return объект класса MainPhoto
     * @see MainPhoto#upload(File, String)
     * @see MainPhoto#upload(File)
     * @see MainPhoto#upload(String, File, String)
     */
    public MainPhoto upload(String owner_id, File file) {
        return upload(owner_id,file, "");
    }

    /**
     * Загрузка основной фотографии группы
     * @param owner_id идентификатор сообщества или текущего пользователя
     * @param file фото
     * @param square_crop квадратная миниатюра фото в формате "x,y,w" (без кавычек), где x и y — координаты верхнего правого угла миниатюры, а w — сторона квадрата
     * @return объект класса MainPhoto
     * @see MainPhoto#upload(File, String)
     * @see MainPhoto#upload(File)
     * @see MainPhoto#upload(String, File)
     */
    public MainPhoto upload(String owner_id, File file, String square_crop) {
        String upload_url = vkApi.photos.method(Photos.getOwnerPhotoUploadServer).params(
                "owner_id=" + owner_id).execute().get("upload_url").toString();
        response = new JsonHandler(BodyOfRequest.mainPhoto(upload_url, file, square_crop));
        return this;
    }

    /**
     * Сохранение результата
     * @return объект класса JsonHandler
     */
    public JsonHandler save() {
        return vkApi.photos.method(Photos.saveOwnerPhoto).params(
                "server=" + response.get("server"),
                "hash=" + response.get("hash"),
                "photo=" + response.get("photo")
        ).execute();
    }
}
