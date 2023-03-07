package com.converterdoit.telegramBot.service;


import com.converterdoit.telegramBot.TelegramBotApplication;
import com.converterdoit.telegramBot.config.configBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final configBot config;

    public TelegramBot(configBot config){
        this.config = config;
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

        sendMessage(chatId, sb.toString());

    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
