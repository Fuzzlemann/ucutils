package de.fuzzlemann.ucutils.commands.faction.medic;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@Mod.EventBusSubscriber
public class ARecipeGiveCommand {

    private static final Pattern RECIPE_GIVE_PATTERN = Pattern.compile("^Du hast ((?:\\[UC])*[a-zA-Z0-9_]+) ein Rezept für (Antibiotika|Hustensaft|Schmerzmittel) ausgestellt\\.$");
    private static String target;
    private static String medication;
    private static int recipeGiveAmountLeft;

    @Command(value = "arezept", usage = "/arezept [Spieler] [Antibiotika/Hustensaft/Schmerzmittel] [Menge]")
    public boolean onCommand(UPlayer p, String target, String medication, int recipeGiveAmount) {

        // if no permitted medication was used and the number of recipes is 0, return false
        if (!medication.equalsIgnoreCase("Antibiotika") && !medication.equalsIgnoreCase("Hustensaft") && !medication.equalsIgnoreCase("Schmerzmittel") || recipeGiveAmount < 1)
            return false;

        // the number of remaining recipes is reduced by 1 because a recipe is given directly to trigger the ClientChatReceivedEvent
        recipeGiveAmountLeft = recipeGiveAmount-1;
        p.sendChatMessage("/rezept " + (ARecipeGiveCommand.target = target) + " " + (ARecipeGiveCommand.medication = medication));
        return true;
    }

    @SubscribeEvent
    public static void onRecipeGiveFeedback(ClientChatReceivedEvent e) throws InterruptedException {

        if (recipeGiveAmountLeft < 1) return; //checks if there is an active recipe-give-process

        String msg = e.getMessage().getUnformattedText();
        if (RECIPE_GIVE_PATTERN.matcher(msg).find()) {
            TimeUnit.MILLISECONDS.sleep(1000); // wait because if the patient accepts the recipe too quickly, a spam error occurs
            AbstractionLayer.getPlayer().sendChatMessage("/rezept " + target + " " + medication);
            recipeGiveAmountLeft--;
        } else if (
                // termination conditions
                msg.contains("Du kannst dir nicht selber Rezepte ausstellen.") ||
                msg.contains("Du bist nicht im Krankenhaus.") ||
                msg.contains("Rezepte kann man erst ab Rang 1 ausstellen.") ||
                msg.contains("Spieler zuweit weg.")) {
            recipeGiveAmountLeft = 0;
        }
    }
}
