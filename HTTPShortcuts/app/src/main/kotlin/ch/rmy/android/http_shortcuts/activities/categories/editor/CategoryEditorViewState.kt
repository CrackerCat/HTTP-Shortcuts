package ch.rmy.android.http_shortcuts.activities.categories.editor

import android.graphics.Color
import androidx.compose.runtime.Stable
import ch.rmy.android.http_shortcuts.activities.categories.editor.models.CategoryBackground
import ch.rmy.android.http_shortcuts.data.enums.CategoryBackgroundType
import ch.rmy.android.http_shortcuts.data.enums.CategoryLayoutType
import ch.rmy.android.http_shortcuts.data.enums.ShortcutClickBehavior

@Stable
data class CategoryEditorViewState(
    val dialogState: CategoryEditorDialogState? = null,
    val categoryName: String,
    val categoryLayoutType: CategoryLayoutType,
    val categoryBackgroundType: CategoryBackgroundType,
    val categoryClickBehavior: ShortcutClickBehavior?,
    private val originalCategoryName: String,
    private val originalCategoryLayoutType: CategoryLayoutType,
    private val originalCategoryBackgroundType: CategoryBackgroundType,
    private val originalCategoryClickBehavior: ShortcutClickBehavior?,
) {
    val hasChanges: Boolean =
        categoryName != originalCategoryName ||
            categoryLayoutType != originalCategoryLayoutType ||
            categoryBackgroundType != originalCategoryBackgroundType ||
            categoryClickBehavior != originalCategoryClickBehavior

    val saveButtonEnabled: Boolean =
        hasChanges && categoryName.isNotBlank()

    val colorButtonVisible: Boolean
        get() = categoryBackgroundType is CategoryBackgroundType.Color

    val backgroundColor: Int
        get() = (categoryBackgroundType as? CategoryBackgroundType.Color)?.color ?: Color.WHITE

    val backgroundColorAsText: String
        get() = (categoryBackgroundType as? CategoryBackgroundType.Color)?.getHexString() ?: ""

    val categoryBackground: CategoryBackground
        get() = when (categoryBackgroundType) {
            is CategoryBackgroundType.Default -> CategoryBackground.DEFAULT
            is CategoryBackgroundType.Color -> CategoryBackground.COLOR
            is CategoryBackgroundType.Wallpaper -> CategoryBackground.WALLPAPER
        }
}
