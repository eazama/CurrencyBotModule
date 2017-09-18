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
import ytt.dijidori.discordbot.currency.CurrencyCore.Currency;
import ytt.dijidori.discordbot.core.TimerCore;

/**
 *
 * @author Eric
 */
public class ExchangeCommand extends AbstractCommand {

    @Override
    public String getKeyword() {
        return "Exchange";
    }

    @Override
    public String getHelp() {
        return "Usage: lolidk";
    }

    @Override
    public void runCommand(MessageReceivedEvent e) {
        String[] msgWrd = e.getMessage().getContent().split(" ");
        if (msgWrd.length != 4) {
            e.getChannel().sendMessage(getHelp());
        }

        long v;
        try {
            v = Long.parseLong(msgWrd[1]);
        } catch (Exception ex) {
            e.getChannel().sendMessage(msgWrd[1] + " is not a valid value");
            return;
        }

        String userID = e.getAuthor().getId();
        CurrencyCore cCore = CurrencyCore.getCore();

        Currency source = cCore.getCurrency(msgWrd[2]);
        if (source == null) {
            e.getChannel().sendMessage(msgWrd[2] + " is not a valid currency");
            return;
        }

        Currency target = cCore.getCurrency(msgWrd[3]);
        if (target == null) {
            e.getChannel().sendMessage(msgWrd[3] + " is not a valid currency");
            return;
        }

        long sourceValue = cCore.getValue(userID, msgWrd[2]);
        long targetValue = cCore.getValue(userID, msgWrd[3]);

        if (sourceValue < v) {
            e.getChannel().sendMessage(String.format("You only have %s%d", source.getSymbol(), v));
            return;
        }
        
        cCore.setValue(sourceValue - Math.round(v * source.getExRate()), msgWrd[1], userID);
        cCore.setValue(sourceValue + Math.round(v * target.getExRate()), msgWrd[2], userID);

        String format = "%s exchanged %s for %s!";

        e.getChannel().sendMessage(String.format(format,
                e.getAuthor().getUsername(),
                source.getDisplayString(v),
                target.getDisplayString(Math.round(v * target.getExRate()))));
    }

}
