package sh.astrid.filter


import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * A String extension function to convert a string into a deserialized MiniMessage component.
 *
 * @return A MiniMessage component
 */
fun String.mm(): Component {
    val mm = MiniMessage.miniMessage()
    val errorResolver = Placeholder.parsed("error", "<#ff6e6e>⚠ <#ff7f6e>")
    val warnResolver = Placeholder.parsed("warn", "<y>⚠")
    val successResolver = Placeholder.parsed("success", "<g>✔")

    return mm.deserialize(this, *getMMResolvers(), successResolver, warnResolver, errorResolver)
        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

fun getMMResolvers(): Array<TagResolver> {
    return listOf<TagResolver>(
        TagResolver.resolver("g", Tag.styling(TextColor.color(191, 255, 198))),
        TagResolver.resolver("y", Tag.styling(TextColor.color(240, 245, 171))),
        TagResolver.resolver("r", Tag.styling(TextColor.fromHexString("#ff7f6e")!!)),
    ).toTypedArray()
}

/**
 * Sends a parsed MiniMessage component
 *
 * @param message The message to parse
 * @return A MiniMessage component
 */
fun CommandSender.send(message: String) {
    this.sendMessage(message.mm())
}


/**
 * Sends a parsed MiniMessage component
 *
 * @param message The message to parse
 * @return A MiniMessage component
 */
fun Player.send(message: String) {
    this.sendMessage(message.mm())
}
