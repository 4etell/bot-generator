package com.foretell.subsystem.telegram.handler;


import com.foretell.rabbit.listener.dto.FileDto;
import com.foretell.rabbit.listener.dto.MessageFileDto;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.partition;

@Singleton
public class TelegramMessageFileHandler implements TelegramMessageHandler<MessageFileDto> {
    @Override
    public void handle(DefaultAbsSender bot, MessageFileDto message) throws TelegramApiException {
        SendMessage sendMessage = getBasicSendMessage(message);
        sendMessage.setText(message.getText());
        bot.execute(sendMessage);

        List<FileDto> documents = new ArrayList<>();
        List<FileDto> photos = new ArrayList<>();

        for (FileDto file : message.getFiles()) {
            if (file.isPicture()) {
                photos.add(file);
            } else {
                documents.add(file);
            }
        }

        // SendMediaGroup поддерживает только 10 вложений, поэтому будем отправлять пачками по 10, если их допустим 20.
        // P.S. Думаю если хотим логику ограничения количества файлов, ее нужно не тут делать.
        List<List<FileDto>> photosLists = partition(photos, 10);
        List<List<FileDto>> documentsLists = partition(documents, 10);

        for (List<FileDto> photosList : photosLists) {
            sendPhotos(photosList, bot, message.getChatId());
        }
        for (List<FileDto> documentsList : documentsLists) {
            sendDocuments(documentsList, bot, message.getChatId());
        }
    }

    private void sendPhotos(List<FileDto> photos, DefaultAbsSender bot, String chatId) throws TelegramApiException {
        if (!photos.isEmpty()) {
            if (photos.size() == 1) {
                FileDto fileDto = photos.get(0);
                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .parseMode("HTML")
                        .chatId(chatId)
                        .photo(new InputFile(new File(fileDto.getFileLink())))
                        .build();
                bot.execute(sendPhoto);
            } else {
                SendMediaGroup photoMediaGroup = SendMediaGroup.builder().chatId(chatId).medias(new ArrayList<>()).build();
                for (FileDto fileDto : photos) {
                    File file = new File(fileDto.getFileLink());
                    photoMediaGroup.getMedias().add(InputMediaPhoto.builder().isNewMedia(true).media("attach://" + file.getName()).mediaName(file.getName()).newMediaFile(file).build());
                }
                bot.execute(photoMediaGroup);
            }
        }
    }

    private void sendDocuments(List<FileDto> documents, DefaultAbsSender bot, String chatId) throws TelegramApiException {
        if (!documents.isEmpty()) {
            if (documents.size() == 1) {
                FileDto fileDto = documents.get(0);
                SendDocument sendDocument = SendDocument
                        .builder()
                        .parseMode("HTML")
                        .chatId(chatId)
                        .document(new InputFile(new File(fileDto.getFileLink())))
                        .build();
                bot.execute(sendDocument);
            } else {
                SendMediaGroup documentMediaGroup = SendMediaGroup.builder().chatId(chatId).medias(new ArrayList<>()).build();
                for (FileDto fileDto : documents) {
                    File file = new File(fileDto.getFileLink());
                    documentMediaGroup.getMedias().add(InputMediaDocument.builder().isNewMedia(true).media("attach://" + file.getName()).mediaName(file.getName()).newMediaFile(file).build());
                }
                bot.execute(documentMediaGroup);
            }
        }
    }

    @Override
    public Class<MessageFileDto> getGenericParameter() {
        return MessageFileDto.class;
    }
}
