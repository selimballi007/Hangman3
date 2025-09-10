package org.hangman3.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface AdsService {
    fun loadInterstitial()
    fun showInterstitial()
}

expect fun provideAdsService(): AdsService

@Composable
expect fun BannerAdView(adUnitId: String, modifier: Modifier = Modifier)

interface RewardedAdService {
    fun loadRewardedAd()

    fun showRewardedAd(onReward: () -> Unit)
}

expect fun provideRewardedAdService(): RewardedAdService