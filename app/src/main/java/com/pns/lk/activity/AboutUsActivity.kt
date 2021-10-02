package com.pns.lk.activity

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pns.lk.BuildConfig
import com.pns.lk.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*


class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.logo)
            .setDescription(getString(R.string.description))
            .addItem(Element("Version " + BuildConfig.VERSION_NAME, R.drawable.version))
            .addGroup("Connect with us")
            .addEmail("pnslabslk@gmail.com")
            .addWebsite("https://pnslab.lk/")
            .addFacebook("PNS Labs")
            .addTwitter("PNS Labs")
            .addYoutube("PNS Labs")
            .addPlayStore("PNS Labs")
            .addInstagram("PNS Labs")
            .addItem(getCopyRightsElement())
            .create()

        setContentView(aboutPage)
    }

    private fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights =
            String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR))
        copyRightsElement.title = copyrights
        copyRightsElement.iconDrawable = R.drawable.about_icon_copy_right
        copyRightsElement.autoApplyIconTint = true
        copyRightsElement.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        copyRightsElement.iconNightTint = android.R.color.white
        copyRightsElement.gravity = Gravity.CENTER
        return copyRightsElement
    }
}