package com.github.heatalways.longPollApi.userLongPoll;

import com.github.heatalways.jsonHandler.JsonHandler;
import com.github.heatalways.VkApi;
import com.github.heatalways.longPollApi.LongPollObject;
import com.github.heatalways.objects.messages.Messages;
import com.github.heatalways.utils.Request;

/**
 * Класс для работы с UserLongPollApi.
 * Метод "onResponse(JsonHandler response)" нужно переопределить для получения данных!
 * Подробнее на https://vk.com/dev/using_longpoll
 * @author heat"kazyxanovr1@gmail.com"
 *
 */
public class UserLongPollApi extends LongPollObject {
    private final int mode;
    private final int version;

    /**
     * Создает объект класса UserLongPollApi
     * @param vkApi объект класса VkApi
     * @param need_pts возращать поле pts{1,0}
     * @param group_id идентификатор группы, от имени которой будет осуществляться работа с API
     * @param wait время ожидания ответа
     * @param mode дополнительные опции ответа
     * @param version версия
     */
    public UserLongPollApi(VkApi vkApi, int need_pts, String group_id, int wait, int mode, int version) {
        JsonHandler response = vkApi.messages.method(Messages.getLongPollServer).params(
                "need_pts=" + need_pts,
                "group_id=" + group_id,
                "lp_version=" + version).execute();
        this.key = response.get("key").toString();
        this.server = response.get("server").toString();
        this.ts = response.get("ts").toString();
        this.mode = mode;
        this.wait = wait;
        this.version = version;
    }

    @Override
    public void makeRequest() {
        String url = "https://" + server +
                "?act=a_check&key=" + key +
                "&ts=" + ts +
                "&wait=" + wait +
                "&mode=" + mode +
                "&version=" + version;
        JsonHandler response = Request.getCallBackResponse(url);
        ts = response.get("ts").toString();
        ((UserMessageHandler)messageHandlerObject).onResponse(response.get("updates"));
    }
}
