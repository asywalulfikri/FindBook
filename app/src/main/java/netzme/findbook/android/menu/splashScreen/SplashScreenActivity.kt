package netzme.findbook.android.menu.splashScreen

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseActivity
import netzme.findbook.android.menu.MainActivity


class SplashScreenActivity : BaseActivity() {

    var splashTread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        starAnimation()
    }

    private fun starAnimation() {
        var anim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim.reset()
        lin_lay.clearAnimation()
        lin_lay.startAnimation(anim)
        anim = AnimationUtils.loadAnimation(this, R.anim.translate)
        anim.reset()

        splash.clearAnimation()
        splash.startAnimation(anim)
        splashTread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100)
                        waited += 100
                    }
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                } catch (e: InterruptedException) { // do nothing
                } finally {
                    finish()
                }
            }
        }
        (splashTread as Thread).start()
    }
}
