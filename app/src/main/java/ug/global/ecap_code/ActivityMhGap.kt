package ug.global.ecap_code

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.MODE_READ_ONLY
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ug.global.ecap_code.databinding.ActivityTrainingBinding
import ug.global.ecap_code.util.URLS
import java.io.File
import java.io.IOException
import java.net.URL

class ActivityMhGap : AppCompatActivity() {
    val binding by lazy { ActivityTrainingBinding.inflate(layoutInflater) }
    private lateinit var pdfRenderer: PdfRenderer
    private var currentPage: PdfRenderer.Page? = null

    private var pageIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.completeModule.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("not_trained", false).apply()
            finish()
            startActivity(Intent(this, ActivityMain::class.java))
        }
        binding.buttonPrevious.setOnClickListener {
            changePage(false)
        }
        binding.buttonNext.setOnClickListener {
            changePage(true)
        }
        lifecycleScope.launch {
            val file = downloadUrl()
            try {


                val descriptor = ParcelFileDescriptor.open(file, MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(descriptor)
                if (pdfRenderer.pageCount > 0) {
                    pageIndex = -1
                }
                MainScope().launch {
                    changePage(true)
                }
            } catch (ex: Exception) {
            }
        }
    }

    private fun changePage(isNext: Boolean) {
        pageIndex = if (isNext) {
            pageIndex + 1
        } else {
            pageIndex - 1
        }
        currentPage?.close()
        currentPage = pdfRenderer.openPage(pageIndex)
        val bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)
        currentPage?.render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)
        binding.pdfView.setImageBitmap(bitmap)
        binding.buttonNext.isEnabled = pageIndex < pdfRenderer.pageCount - 1
        binding.buttonPrevious.isEnabled = pageIndex > 0
    }

    private suspend fun downloadUrl(): File? {
        return withContext(IO) {
            try {
                val inputStream = URL(
                    URLS.BASE_URL + PreferenceManager.getDefaultSharedPreferences(this@ActivityMhGap).getString("ecap_file", "none")
                ).openStream()
                val file = File(cacheDir, "training.pdf")
                file.outputStream().use {
                    inputStream.copyTo(it)
                }
                MainScope().launch {
                    binding.progressBar5.visibility = View.GONE
                }
                return@withContext file
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }
}