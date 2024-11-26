package ug.global.ecap_code.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ug.global.ecap_code.R


class EcapTextHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val counter: TextView
    private val header: TextView

    init {
        // Inflate the layout
        val view = LayoutInflater.from(context).inflate(R.layout.custom_text_header, this, true)

        // Initialize views
        counter = view.findViewById(R.id.numberText)
        header = view.findViewById(R.id.headerText)

        // Handle any custom attributes (if needed)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EcapTextHeader, 0, 0)
            counter.text = typedArray.getString(R.styleable.EcapTextHeader_counter) ?: "  "
            header.text = typedArray.getString(R.styleable.EcapTextHeader_header) ?: ""
            typedArray.recycle()
        }
    }

    fun setHeader(string: String) {
        header.text = string
    }

}

