package some.code.testforavito

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TopSpacingItemDecoration(private val padding: Int): RecyclerView.ItemDecoration(){



    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.right = padding
        outRect.bottom = padding
        outRect.left = padding


    }
}

