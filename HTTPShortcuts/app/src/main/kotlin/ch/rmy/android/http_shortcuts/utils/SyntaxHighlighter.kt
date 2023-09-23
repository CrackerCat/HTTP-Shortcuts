package ch.rmy.android.http_shortcuts.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.theme.DefaultTheme
import com.wakaztahir.codeeditor.highlight.theme.SyntaxColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SyntaxHighlighter(
    private val language: String,
    useDarkTheme: Boolean,
) {

    private val theme = if (useDarkTheme) {
        DarkTheme
    } else {
        DefaultTheme()
    }

    fun format(text: String): AnnotatedString =
        buildAnnotatedString {
            append(text)
            applyFormatting(this, text)
        }

    fun applyFormatting(builder: AnnotatedString.Builder, text: String) {
        if (text.length > MAX_LENGTH) {
            return
        }
        with(builder) {
            parser.parse(language, text).forEach {
                addStyle(theme.toSpanStyle(it), it.offset, it.offset + it.length)
            }
        }
    }

    private object DarkTheme : CodeTheme() {
        override val colors = SyntaxColors(
            type = Color(0xFF1FEED3),
            keyword = Color(0xFF1BEBCF),
            literal = Color(0xFFbfb9b0),
            comment = Color(0xFFFFC263),
            string = Color(0xFFc27905),
            punctuation = Color(0xFFB4A794),
            plain = Color(0xFFfff5e6),
            tag = Color(0xFF6479FF),
            declaration = Color(0xFFbdb3b6),
            source = Color(0xFFc27905),
            attrName = Color(0xFF929ff7),
            attrValue = Color(0xFFc27905),
            nocode = Color(0xFFfff5e6),
        )
    }

    object Languages {
        const val JS = "js"
    }

    companion object {
        /**
         * Performance is impaired too much when the text gets too long, so we rather disable syntax highlighting altogether
         * after the maximum length has been reached.
         */
        const val MAX_LENGTH = 4_000

        private val parserDeferred: Deferred<PrettifyParser> =
            CoroutineScope(Dispatchers.Default).async {
                PrettifyParser()
            }

        internal val parser
            get() = runBlocking { parserDeferred.await() }
    }
}
