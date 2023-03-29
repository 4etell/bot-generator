package com.foretell.handler;

import com.foretell.flow.StateService;
import com.foretell.flow.dto.response.FlowResponseDto;
import com.foretell.flow.dto.response.impl.TextFlowResponseDto;
import com.foretell.flow.dto.response.impl.menu.MenuFlowResponseDto;
import com.foretell.flow.FlowResponseType;
import com.foretell.handler.mapper.MenuMapper;
import com.foretell.rabbit.client.BotMessageClient;
import com.foretell.rabbit.client.dto.AbstractMessageDto;
import com.foretell.rabbit.client.dto.impl.menu.data.MenuRowDto;
import com.foretell.rabbit.client.dto.impl.menu.MessageMenuDto;
import com.foretell.rabbit.client.dto.impl.text.MessageTextDto;
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
                .flatMapMany(state -> Flux.fromIterable(state.response()))
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


    private Flux<AbstractMessageDto> getAnswer(UserMessageDto messageEntity, FlowResponseDto response) {
        String chatId = String.valueOf(messageEntity.getChatId());
        FlowResponseType flowResponseType = response.getResponseType();
        switch (flowResponseType) {
            case TEXT_RESPONSE -> {
                TextFlowResponseDto textResponse = (TextFlowResponseDto) response;
                return Flux.just(new MessageTextDto(chatId, textResponse.text()));
            }
            case MENU_RESPONSE -> {
                MenuFlowResponseDto menuResponse = (MenuFlowResponseDto) response;
                List<MenuRowDto> rows = menuResponse
                        .rows()
                        .stream()
                        .map(menuRow -> menuMapper.menuRowToMenuRowDto(menuRow))
                        .toList();
                return Flux.just(new MessageMenuDto(chatId, menuResponse.text(), rows));
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
