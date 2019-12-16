package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.info.InfoStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class FCInfoCommand {

    @Command({"fcinfo", "factioncommandinfo", "fcommandinfo", "factioncinfo"})
    public boolean onCommand(@CommandParam(joinStart = true, required = false, defaultValue = CommandParam.NULL) Faction faction) {
        if (faction != null) {
            InfoStorage.factionInfoMap.get(faction).constructCommandHelpMessage().send();
            return true;
        }

        Message.builder()
                .joiner(InfoStorage.factionInfoMap.values())
                .consumer(((builder, factionInfo) -> builder.messageParts(factionInfo.constructClickableMessage("/fcinfo " + factionInfo.getShortName()).getMessageParts())))
                .newLineJoiner().advance()
                .send();
        return true;
    }
}
