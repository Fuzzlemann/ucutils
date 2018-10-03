package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class DeleteFriendConfirmEventHandler {

    @SubscribeEvent
    public static void onChatSend(ClientChatEvent e) {
        String message = e.getMessage();
        if (!message.startsWith("/friend delete")) return;

        String[] splitted = message.split(" ");
        if (splitted.length <= 2) return;

        String name = splitted[2];
        if (splitted.length > 3 && splitted[3].equals("confirm")) {
            e.setMessage("/Friend delete " + name);
            return;
        }

        Message.MessageBuilder prefixBuilder = Message.builder();

        prefixBuilder.of("[").color(TextFormatting.DARK_GRAY).advance()
                .of("FreundesListe").color(TextFormatting.LIGHT_PURPLE).advance()
                .of("]").color(TextFormatting.DARK_GRAY).advance();

        List<MessagePart> prefixComponents = prefixBuilder.build().getMessageParts();

        Message.MessageBuilder builder = Message.builder();

        builder.messageParts(prefixComponents)
                .of(" Bestätige, dass du die Freundschaft mit " + name + " beenden willst.\n").color(TextFormatting.GOLD).advance()
                .messageParts(prefixComponents)
                .of(" [").color(TextFormatting.GRAY).advance()
                .of("Bestätigen").color(TextFormatting.GREEN)
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/friend delete " + name + " confirm")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, Message.builder().of("Bestätigen").color(TextFormatting.GREEN).build()).advance()
                .of("]").color(TextFormatting.GRAY).advance();

        Main.MINECRAFT.player.sendMessage(builder.build().toTextComponent());

        e.setCanceled(true);
    }
}
