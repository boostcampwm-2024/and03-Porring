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

public val IconGallery: ImageVector
    get() {
        if (_IconGallery != null) {
            return _IconGallery!!
        }
        _IconGallery = ImageVector.Builder(
            name = "IconGallery",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
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
                moveTo(24f, 16.8f)
                verticalLineTo(2.4f)
                curveTo(240f, 1.080f, 22.920f, 00f, 21.60f, 00f)
                horizontalLineTo(7.2f)
                curveTo(5.880f, 00f, 4.80f, 1.080f, 4.80f, 2.40f)
                verticalLineTo(16.8f)
                curveTo(4.80f, 18.120f, 5.880f, 19.20f, 7.20f, 19.20f)
                horizontalLineTo(21.6f)
                curveTo(22.920f, 19.20f, 240f, 18.120f, 240f, 16.80f)
                close()
                moveTo(10.8f, 12f)
                lineTo(13.236f, 15.252f)
                lineTo(16.8f, 10.8f)
                lineTo(21.6f, 16.8f)
                horizontalLineTo(7.2f)
                lineTo(10.8f, 12f)
                close()
                moveTo(0f, 4.8f)
                verticalLineTo(21.6f)
                curveTo(00f, 22.920f, 1.080f, 240f, 2.40f, 240f)
                horizontalLineTo(19.2f)
                verticalLineTo(21.6f)
                horizontalLineTo(2.4f)
                verticalLineTo(4.8f)
                horizontalLineTo(0f)
                close()
            }
        }.build()
        return _IconGallery!!
    }

private var _IconGallery: ImageVector? = null
