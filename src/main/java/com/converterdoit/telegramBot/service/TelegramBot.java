package com.converterdoit.telegramBot.service;


import com.converterdoit.telegramBot.TelegramBotApplication;
import com.converterdoit.telegramBot.config.configBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final configBot config;
    static final String HELP_TEXT = "❤️ This bot was created to help in everyday affairs and solve small issues. ❤️ \n\n" +
            "Type /start to see a welcome message";

    public TelegramBot(configBot config){
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Start bot"));
        listofCommands.add(new BotCommand("/string", "Substring method"));
        listofCommands.add(new BotCommand("/weather", "Check current weather"));
        listofCommands.add(new BotCommand("/id", "Check your ID"));
        listofCommands.add(new BotCommand("/converter", "Restart the bot"));
        listofCommands.add(new BotCommand("/mydata", "Get your data"));
        listofCommands.add(new BotCommand("/deletedata", "Delete your data"));
        listofCommands.add(new BotCommand("/help", "Help your data"));
        listofCommands.add(new BotCommand("/settings", "Set preferences"));
        try{
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }catch(TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }



    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/help":
                        sendMessage(chatId, HELP_TEXT);
                        log.info("/help used: " + update.getMessage().getChat().getFirstName());
                        log.info("BIO user: " + update.getMessage().getChat().getBio());
                        break;
                case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                default:
                        sendMessage(chatId, "Ssory, command has not found");
            }
        }
    }
    @Override
    public String getBotToken(){
        return config.getToken();
    }
    private void startCommandReceived(long chatId, String name){
        StringBuilder sb = new StringBuilder();

        sb.append("Hi " + name + ", welcome to the InnerBot!");
        sb.append("\nPlease, choose option:");

        log.info("Replied to user: " + name);

        sendMessage(chatId, sb.toString());

    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        } catch (TelegramApiException e) {
            log.error("E-occurred: " + e.getMessage());
        }
    }
}
