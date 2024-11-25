package ug.global.ecap_code.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.radiobutton.MaterialRadioButton
import ug.global.ecap_code.R

class EcapRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val radioGroup: RadioGroup
    private val radioYes: MaterialRadioButton
    private val radioNo: MaterialRadioButton

    init {
        // Inflate the layout
        val view = LayoutInflater.from(context).inflate(R.layout.custom_radio_group, this, true)

        // Initialize views
        textView = view.findViewById(R.id.customTextView)
        radioGroup = view.findViewById(R.id.customRadioGroup)
        radioYes = view.findViewById(R.id.radioYes)
        radioNo = view.findViewById(R.id.radioNo)

        // Handle any custom attributes (if needed)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EcapRadioGroup, 0, 0)
            val text = typedArray.getString(R.styleable.EcapRadioGroup_text)
            setText(text ?: "")
            val defaultChecked = typedArray.getInt(R.styleable.EcapRadioGroup_defaultChecked, 1)
            when (defaultChecked) {
                1 -> radioYes.isChecked = true // Default to "yes"
                2 -> radioNo.isChecked = true  // Default to "no"
            }
            typedArray.recycle()
        }
    }

    // Set the text for the TextView
    private fun setText(text: String) {
        textView.text = text
    }

    fun isYes(): Boolean {
        return radioYes.isChecked
    }

    fun setIsYes(): Boolean {
        return false
    }

    fun setOnCheckedChangeListener(listener: (isYes: Boolean) -> Unit) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioYes -> listener(true)
                R.id.radioNo -> listener(false)
            }
        }
    }
}

