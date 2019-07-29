package com.bitauto.heart_net.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitauto.heart_net.R

/**
 *    @author : sunbq
 *    e-mail : subbq@yiche.com
 *    date   : 2019-07-2911:24
 *    desc   : 功能列表
 */
class FunctionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function)
    }

    companion object {
        fun start(context: Context?) {
            context?.startActivity(
                Intent(
                    context,
                    FunctionActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}