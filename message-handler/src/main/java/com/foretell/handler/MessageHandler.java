package com.foretell.handler;

import com.foretell.flow.StateService;
import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.dto.response.impl.TextResponseDto;
import com.foretell.flow.dto.response.impl.menu.MenuResponseDto;
import com.foretell.flow.model.response.ResponseType;
import com.foretell.handler.mapper.MenuMapper;
import com.foretell.rabbit.client.BotMessageClient;
import com.foretell.rabbit.client.dto.AbstractMessageDto;
import com.foretell.rabbit.client.dto.MenuRowDto;
import com.foretell.rabbit.client.dto.MessageMenuDto;
import com.foretell.rabbit.client.dto.MessageTextDto;
import com.foretell.rabbit.listener.dto.UserMessageDto;
import com.foretell.util.PropertyService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;


@Singleton
@Slf4j
public class MessageHandler {

    @Inject
    private BotMessageClient botMessageClient;

    @Inject
    private MenuMapper menuMapper;

    @Inject
    private StateService stateService;

    @Inject
    private PropertyService propertyService;

    public void handleMessage(UserMessageDto userMessageDto) {
        stateService.getStateByCommand(userMessageDto.getCommand())
                .flatMapMany(state -> Flux.fromIterable(state.getResponse()))
                .flatMap(response -> getAnswer(userMessageDto, response)
                        .doOnError(e -> log.error("Error occurred while getting answer:", e))
                        .onErrorResume(e -> Flux.just(getAnswerErrorMessage(userMessageDto)))
                )
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        botMessageClient::sendMessage,
                        error -> {
                            log.error("Error during handle message", error);
                            botMessageClient.sendMessage(getStateErrorMessage(userMessageDto));
                        }
                );
    }


    private Flux<AbstractMessageDto> getAnswer(UserMessageDto messageEntity, ResponseDto response) {
        String chatId = String.valueOf(messageEntity.getChatId());
        ResponseType responseType = response.getResponseType();
        switch (responseType) {
            case TEXT_RESPONSE -> {
                TextResponseDto textResponse = (TextResponseDto) response;
                return Flux.just(new MessageTextDto(chatId, textResponse.getText()));
            }
            case MENU_RESPONSE -> {
                MenuResponseDto menuResponse = (MenuResponseDto) response;
                log.info("Menu responseDto {}", menuResponse);
                List<MenuRowDto> rows = menuResponse
                        .getRows()
                        .stream()
                        .map(menuRow -> menuMapper.menuRowToMenuRowDto(menuRow))
                        .toList();
                return Flux.just(new MessageMenuDto(chatId, menuResponse.getText(), rows));
            }
            default -> {
                return Flux.error(new IllegalArgumentException("Cannot recognize responseType"));
            }
        }
    }

    private AbstractMessageDto getAnswerErrorMessage(UserMessageDto userMessageDto) {
        return new MessageTextDto(
                String.valueOf(userMessageDto.getChatId()),
                propertyService.getMessageProperty("something_wrong_message"));
    }

    private AbstractMessageDto getStateErrorMessage(UserMessageDto userMessageDto) {
        return new MessageTextDto(
                String.valueOf(userMessageDto.getChatId()),
                propertyService.getMessageProperty("not_state_found_message"));
    }
}
