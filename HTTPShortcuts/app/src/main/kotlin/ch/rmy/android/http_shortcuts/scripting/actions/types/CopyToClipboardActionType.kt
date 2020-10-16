package ch.rmy.android.http_shortcuts.scripting.actions.types

import ch.rmy.android.http_shortcuts.scripting.ActionAlias
import ch.rmy.android.http_shortcuts.scripting.actions.ActionDTO

class CopyToClipboardActionType : BaseActionType() {

    override val type = TYPE

    override fun fromDTO(actionDTO: ActionDTO) = CopyToClipboardAction(
        text = actionDTO[KEY_TEXT] ?: "",
    )

    override fun getAlias() = ActionAlias(
        functionName = FUNCTION_NAME,
        parameters = listOf(KEY_TEXT),
    )

    companion object {

        const val TYPE = "copy_to_clipboard"
        const val FUNCTION_NAME = "copyToClipboard"

        const val KEY_TEXT = "text"

    }

}