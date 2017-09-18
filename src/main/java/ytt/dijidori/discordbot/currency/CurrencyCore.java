/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ytt.dijidori.discordbot.currency;

import java.sql.ResultSet;
import org.apache.commons.configuration.PropertiesConfiguration;
import ytt.dijidori.discordbot.core.Core;
import ytt.dijidori.discordbot.core.ConfigCore;
import ytt.dijidori.discordbot.core.UserCore;

/**
 *
 * @author Eric
 */
public class CurrencyCore {

    private static CurrencyCore core;
    private final PropertiesConfiguration prop;

    private CurrencyCore() {
        prop = ConfigCore.getCore().getConfig("currency");
        prop.setThrowExceptionOnMissing(true);
    }

    public static CurrencyCore getCore() {
        if (core == null) {
            core = new CurrencyCore();
        }
        return core;
    }

    public long getValue(String userID, String currency) {
        String ret = UserCore.getCore().getProperty(userID, currency, "0");
        try {
            return Long.parseLong(ret);
        } catch (Exception ex) {
            System.out.println("Error in PermissionsInfo.getRoleValue");
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public void setValue(long value, String currency, String userID) {
        UserCore.getCore().setProperty(userID, currency, value);
    }

    public Currency getCurrency(String name) {
        String symbol;
        float exRate;
        try {
            symbol = prop.getString(name + ".symbol");
            exRate = prop.getFloat(name + ".exRate");
        } catch (Exception ex) {
            System.err.println("Unable to load Currency " + name);
            return null;
        }
        return new Currency(name, symbol, exRate);
    }

    public class Currency {

        private String name;
        private String symbol;
        private float exRate;

        Currency(String name, String symbol, float exRate) {
            this.name = name;
            this.symbol = symbol;
            this.exRate = exRate;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public float getExRate() {
            return exRate;
        }
        
        public String getDisplayString(long value){
            return String.format("%s%d", getSymbol(), value);
        }
    }
}
