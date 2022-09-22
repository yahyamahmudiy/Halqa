package com.example.halqa.dialog

import android.app.Dialog
import android.content.Context
import com.example.halqa.R


class DownloadSuccessDialog(context: Context) :
    Dialog(context, R.style.CustomDialogTheme) {

    init {
        this.setContentView(R.layout.layout_download_success)
    }

}