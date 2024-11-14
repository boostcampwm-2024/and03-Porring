/*
* Converted using https://composables.com/svgtocompose
*/

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val IconFollow: ImageVector
	get() {
		if (_IconFollow != null) {
			return _IconFollow!!
		}
		_IconFollow = ImageVector.Builder(
            name = "IconFollow",
            defaultWidth = 24.dp,
            defaultHeight = 18.dp,
            viewportWidth = 24f,
            viewportHeight = 18f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFF834FE0)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(15.2727f, 8.72727f)
				curveTo(17.68360f, 8.72730f, 19.63640f, 6.77450f, 19.63640f, 4.36360f)
				curveTo(19.63640f, 1.95270f, 17.68360f, 00f, 15.27270f, 00f)
				curveTo(12.86180f, 00f, 10.90910f, 1.95270f, 10.90910f, 4.36360f)
				curveTo(10.90910f, 6.77450f, 12.86180f, 8.72730f, 15.27270f, 8.72730f)
				close()
				moveTo(5.45455f, 6.54545f)
				verticalLineTo(3.27273f)
				horizontalLineTo(3.27273f)
				verticalLineTo(6.54545f)
				horizontalLineTo(0f)
				verticalLineTo(8.72727f)
				horizontalLineTo(3.27273f)
				verticalLineTo(12f)
				horizontalLineTo(5.45455f)
				verticalLineTo(8.72727f)
				horizontalLineTo(8.72727f)
				verticalLineTo(6.54545f)
				horizontalLineTo(5.45455f)
				close()
				moveTo(15.2727f, 10.9091f)
				curveTo(12.360f, 10.90910f, 6.54540f, 12.37090f, 6.54540f, 15.27270f)
				verticalLineTo(17.4545f)
				horizontalLineTo(24f)
				verticalLineTo(15.2727f)
				curveTo(240f, 12.37090f, 18.18550f, 10.90910f, 15.27270f, 10.90910f)
				close()
			}
		}.build()
		return _IconFollow!!
	}

private var _IconFollow: ImageVector? = null
