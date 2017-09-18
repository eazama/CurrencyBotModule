/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ytt.dijidori.discordbot.command;

import java.util.Random;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import ytt.dijidori.discordbot.currency.CurrencyCore;
import ytt.dijidori.discordbot.core.TimerCore;

/**
 *
 * @author Eric
 */
public class AllowanceCommand extends AbstractCommand {

    private static final String timerID = "swTimer";

    @Override
    public String getKeyword() {
        return "SelfWorth";
    }

    @Override
    public String getHelp() {
        return "Usage: Command only. No Arguments. Can only be run once per day.";
    }

    @Override
    public void runCommand(MessageReceivedEvent e) {
        String userID = e.getAuthor().getId();
        TimerCore tCore = TimerCore.getCore();
        CurrencyCore cCore = CurrencyCore.getCore();
        DateTime date = new DateTime(DateTimeZone.UTC);
        long value = new Random(userID.hashCode() + date.getYear() + date.getDayOfYear()).nextInt(100);
        long savedValue = cCore.getValue(userID, "usd");
        String format = "%s is worth $%d today!\n"
                + "%s is worth $%d overall!";
        if (tCore.hasTimerExpired(userID, timerID)) {
            savedValue += value;
            cCore.setValue(savedValue, "usd", userID);
            tCore.setTimer(date.withTime(0, 0, 0, 0).plusDays(1).getMillis(), timerID, userID);
        }
        e.getChannel().sendMessage(String.format(format,
                e.getAuthor().getUsername(), value,
                e.getAuthor().getUsername(), savedValue));
    }

}
