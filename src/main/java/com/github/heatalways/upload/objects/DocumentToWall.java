package com.github.heatalways.upload.objects;

import com.github.heatalways.objects.docs.Docs;
import com.github.heatalways.objects.wall.Wall;
import com.github.heatalways.VkApi;
import com.github.heatalways.jsonHandler.JsonHandler;
import com.github.heatalways.upload.BodyOfRequest;
import com.github.heatalways.upload.UploadObject;
import com.github.heatalways.utils.ArrayToString;

import java.io.File;

/**
 * Класс для загрузки документа на стену.
 * @author heat"kazyxanovr1@gmail.com"
 */
public class DocumentToWall extends UploadObject {

    /**
     * Конструктор, принимающий в качестве параметра объект класса VkApi
     * @param vkApi объект класса VkApi
     */
    public DocumentToWall(VkApi vkApi) {
        super(vkApi);
    }

    /**
     * Загрузка документа на стену пользователя
     * @param file документ
     * @return объект класса DocumentToWall
     * @see DocumentToWall#upload(String, File)
     */
    public DocumentToWall upload(File file) {
        return upload("", file);
    }

    /**
     * Загрузка документа на стену группы
     * @param group_id идентификатор группы
     * @param file документ
     * @return объект класса DocumentToWall
     * @see DocumentToWall#upload(File)
     */
    public DocumentToWall upload(String group_id, File file) {
        String upload_url = vkApi.docs.method(Docs.getWallUploadServer).params(
                "group_id=" + group_id).execute().get("upload_url").toString();
        response = new JsonHandler(BodyOfRequest.document(upload_url, file));
        return this;
    }

    /**
     * Сохранение результата
     * @param args дополнительные параметры, которые будут использоваться в методах: docs.save
     * @return объект класса DocumentToWall
     */
    public DocumentToWall save(String... args) {
        response = vkApi.docs.method(Docs.save).params(
                "file=" + response.get("file"),
                ArrayToString.toStr(args)
        ).execute().get(0);
        return this;
    }

    /**
     * Публикация на стену
     * @param args дополнительные параметры, которые будут использоваться в методах: wall.post
     * @return объект класса JsonHandler
     */
    public JsonHandler post(String... args) {
        return vkApi.wall.method(Wall.post).params(
                "owner_id=" + response.get("owner_id"),
                "attachments=doc" + response.get("owner_id") + "_" + response.get("id"),
                ArrayToString.toStr(args)
        ).execute();
    }
}
