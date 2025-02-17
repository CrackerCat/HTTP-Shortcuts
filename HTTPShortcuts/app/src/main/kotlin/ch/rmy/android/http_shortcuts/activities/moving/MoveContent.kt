package ch.rmy.android.http_shortcuts.activities.moving

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.moving.models.CategoryItem
import ch.rmy.android.http_shortcuts.components.FontSize
import ch.rmy.android.http_shortcuts.components.HelpText
import ch.rmy.android.http_shortcuts.components.ShortcutIcon
import ch.rmy.android.http_shortcuts.components.Spacing
import ch.rmy.android.http_shortcuts.data.domains.categories.CategoryId
import ch.rmy.android.http_shortcuts.data.domains.shortcuts.ShortcutId
import ch.rmy.android.http_shortcuts.data.dtos.ShortcutPlaceholder
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun MoveContent(
    categories: List<CategoryItem>,
    onShortcutMoved: (ShortcutId, ShortcutId?, CategoryId?) -> Unit,
    onMoveEnded: () -> Unit,
) {
    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            val shortcutId = from.key as ShortcutId
            val targetKey = to.key as String
            if (targetKey.startsWith(CATEGORY_KEY_PREFIX)) {
                onShortcutMoved(shortcutId, null, targetKey.removePrefix(CATEGORY_KEY_PREFIX))
            } else {
                onShortcutMoved(shortcutId, targetKey, null)
            }
        },
        onDragEnd = { _, _ ->
            onMoveEnded()
        },
    )

    Column {
        Box(
            modifier = Modifier.padding(horizontal = Spacing.MEDIUM),
        ) {
            HelpText(text = stringResource(R.string.message_moving_enabled))
        }
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .reorderable(reorderableState)
                .padding(
                    vertical = Spacing.MEDIUM,
                ),
            verticalArrangement = Arrangement.spacedBy(Spacing.SMALL),
        ) {
            categories.forEachIndexed { index, category ->
                item(
                    key = "$CATEGORY_KEY_PREFIX${category.id}",
                    contentType = "category",
                ) {
                    Column {
                        if (index != 0) {
                            Spacer(
                                modifier = Modifier.height(Spacing.MEDIUM),
                            )
                        }
                        CategoryHeader(
                            name = category.name,
                        )

                        if (category.shortcuts.isEmpty()) {
                            EmptyCategoryContent()
                        }
                    }
                }

                items(
                    items = category.shortcuts,
                    key = { it.id },
                    contentType = { "shortcut" },
                ) { item ->
                    ReorderableItem(
                        reorderableState,
                        key = item.id,
                        modifier = Modifier.detectReorderAfterLongPress(reorderableState)
                    ) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                        ShortcutListItem(
                            modifier = Modifier
                                .shadow(elevation.value)
                                .background(MaterialTheme.colorScheme.surface),
                            shortcut = item,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    name: String,
) {
    Text(
        modifier = Modifier
            .padding(horizontal = Spacing.MEDIUM),
        text = name,
        fontSize = FontSize.BIG,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ShortcutListItem(
    modifier: Modifier,
    shortcut: ShortcutPlaceholder,
) {
    Column(
        modifier,
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            headlineContent = {
                Text(
                    text = shortcut.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                ShortcutIcon(shortcut.icon)
            },
        )
        HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.3f))
    }
}

@Composable
private fun EmptyCategoryContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.8f),
            headlineContent = {
                Text(
                    text = stringResource(R.string.placeholder_empty_category_for_moving),
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                )
            },
        )
        HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.3f))
    }
}

private const val CATEGORY_KEY_PREFIX = "category-"
