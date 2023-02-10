package com.example.sunofbeachagain.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.example.sunofbeachagain.R

object MyAnimUtil {
    private lateinit var tr: TranslateAnimation

    private lateinit var tr2: TranslateAnimation

    private lateinit var tr3: TranslateAnimation

    private lateinit var tr4: TranslateAnimation

    private lateinit var tr5: TranslateAnimation

    private lateinit var tr6: TranslateAnimation

    private lateinit var tr7: TranslateAnimation

    private lateinit var tr8: TranslateAnimation



    fun setAnim(context: Context,vararg views: View){
        tr = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr2 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr3 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr4 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr5 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr6 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr7 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation

        tr8 = AnimationUtils.loadAnimation(context,
            R.anim.login_in_anim) as TranslateAnimation



      for (i in views.indices){
          when(i){
              0->{

                 views[i].animation = tr
              }

              1->{
                  tr2.startOffset = 100
                  views[i].animation = tr2
              }

              2->{
                  tr3.startOffset = 200
                  views[i].animation = tr3
              }

              3->{
                  tr4.startOffset = 300
                  views[i].animation = tr4
              }

              4->{
                  tr5.startOffset = 400
                  views[i].animation = tr5
              }

              5->{
                  tr6.startOffset = 500
                  views[i].animation = tr6
              }

              6->{
                  tr7.startOffset = 600
                  views[i].animation = tr7
              }


             else->{
                 tr8.startOffset = 700
                 views[i].animation = tr8
             }
          }
      }


    }
}