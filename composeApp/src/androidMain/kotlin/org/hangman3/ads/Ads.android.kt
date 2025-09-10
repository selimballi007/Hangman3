package org.hangman3.ads

import android.util.Log
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" // test interstitial. replace on publish.

actual fun provideAdsService(): AdsService = AndroidAdsService()

class AndroidAdsService : AdsService {
    @Volatile
    private var interstitialAd: InterstitialAd? = null

    override fun loadInterstitial() {
        val ctx = AndroidAdHolder.appContext ?: return
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(ctx, INTERSTITIAL_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                Log.d("Ads", "Interstitial loaded")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                interstitialAd = null
                Log.w("Ads", "Interstitial failed to load: ${loadAdError.message}")
            }
        })
    }

    override fun showInterstitial() {
        val activity = AndroidAdHolder.currentActivity
        val ad = interstitialAd
        if (ad != null && activity != null) {
            ad.show(activity)
            // gösterildikten sonra yeniden yüklemek istersen burada loadInterstitial() çağırabilirsin
            interstitialAd = null
        } else {
            Log.d("Ads", "Interstitial not ready or activity null")
        }
    }
}

@Composable
actual fun BannerAdView(adUnitId: String, modifier: Modifier) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            AdView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier
    )
}

private const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

actual fun provideRewardedAdService(): RewardedAdService = AndroidRewardedAdService()

class AndroidRewardedAdService : RewardedAdService {
    private var rewardedAd: RewardedAd? = null

    override fun loadRewardedAd() {
        val context = AndroidAdHolder.appContext ?: return
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            TEST_REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) { rewardedAd = ad }
                override fun onAdFailedToLoad(error: LoadAdError) { rewardedAd = null }
            }
        )
    }

    override fun showRewardedAd(onReward: () -> Unit) {
        val activity = AndroidAdHolder.currentActivity ?: return
        rewardedAd?.show(activity) { reward: RewardItem ->
            onReward() // The user watched the ad, extra rights granted
            // Reload after the ad is displayed
            loadRewardedAd()
        }
    }
}