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
public class WalletCommand extends AbstractCommand {

    private static final String timerID = "swTimer";

    @Override
    public String getKeyword() {
        return "Wallet";
    }

    @Override
    public String getHelp() {
        return "glub";
    }

    @Override
    public void runCommand(MessageReceivedEvent e) {
        String[] msgWrd = e.getMessage().getContent().split(" ");
        if(msgWrd.length < 2){
            e.getChannel().sendMessage(getHelp());
            return;
        }
        
        String userID = e.getAuthor().getId();
        CurrencyCore cCore = CurrencyCore.getCore();
        Currency c = cCore.getCurrency(msgWrd[1]);
        if(c == null){
            e.getChannel().sendMessage(msgWrd[1] + " is not a valid currency");
            return;
        }
        
        long value = cCore.getValue(userID, msgWrd[1]);
        String format = "%s has %s%d today!\n";

        e.getChannel().sendMessage(String.format(format,
                e.getAuthor().getUsername(), c.getSymbol(), value));
    }

}
