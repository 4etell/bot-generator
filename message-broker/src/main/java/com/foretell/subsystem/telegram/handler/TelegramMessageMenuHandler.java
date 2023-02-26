package com.foretell.subsystem.telegram.handler;

import com.foretell.rabbit.listener.dto.MenuRowDto;
import com.foretell.rabbit.listener.dto.MessageMenuDto;
import com.foretell.util.PropertyService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Singleton
public class TelegramMessageMenuHandler implements TelegramMessageHandler<MessageMenuDto> {

    @Inject
    private PropertyService propertyService;

    @Override
    public void handle(DefaultAbsSender bot, MessageMenuDto messageMenuDto) throws TelegramApiException {
        SendMessage sendMessage = getBasicSendMessage(messageMenuDto);

        List<MenuRowDto> menuRows = messageMenuDto.getRows();
        if (menuRows != null && !menuRows.isEmpty()) {
            String text = messageMenuDto.getText();
            List<List<Pair<String, String>>> rowsOfPairsTextAndCommand = menuRows.stream().map(
                    row -> row.getItems()
                            .stream()
                            .map(item -> new Pair<>(item.getTitle(), item.getCommand()))
                            .toList()
            ).toList();
            sendMessage.setReplyMarkup(createInlineMenu(rowsOfPairsTextAndCommand));
            if (text != null) {
                sendMessage.setText(text);
            } else {
                sendMessage.setText(propertyService.getMessageProperty("telegram.default.menu.text"));
            }

        } else {
            sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        }
        sendMessage.setParseMode("HTML");
        bot.execute(sendMessage);
    }

    @Override
    public Class<MessageMenuDto> getGenericParameter() {
        return MessageMenuDto.class;
    }

    private InlineKeyboardMarkup createInlineMenu(List<List<Pair<String, String>>> rowsOfPairsTextAndCommand) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = rowsOfPairsTextAndCommand.stream().map(
                pairs -> pairs.stream()
                        .map(pair -> getButton(pair.getFirst(), pair.getSecond()))
                        .toList()
        ).toList();
        keyboardMarkup.setKeyboard(rowsInline);

        return keyboardMarkup;
    }

    private InlineKeyboardButton getButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
