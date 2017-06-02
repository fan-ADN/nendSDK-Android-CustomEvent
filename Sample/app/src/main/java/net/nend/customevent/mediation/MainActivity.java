package net.nend.customevent.mediation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "CustomMediationApp";

    private static final String ADMOB_MEDIATION_REWARD_CUSTOM_EVENT_UNIT_ID = "YOUR_UNIT_ID";
    private RewardedVideoAd mAdMobRewardedVideoAd;
    private FloatingActionButton mAdapterVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // this is sample: adapter rewarded video button.
        mAdapterVideoButton = (FloatingActionButton) findViewById(R.id.fab);
        mAdapterVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdMobRewardedVideoAd.isLoaded()) {
                    mAdMobRewardedVideoAd.show();
                } else {
                    loadCustomEventRewardedVideoAd();
                }
            }
        });

        /**
         * This is sample: adapter rewarded video ad.
         */
        mAdMobRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mAdMobRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                showToastFromAdListener("onRewardedVideoAdLoaded");
                mAdapterVideoButton.setEnabled(true);
            }

            @Override
            public void onRewardedVideoAdOpened() {
                showToastFromAdListener("onRewardedVideoAdOpened");
            }

            @Override
            public void onRewardedVideoStarted() {
                showToastFromAdListener("onRewardedVideoStarted");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                showToastFromAdListener("onRewardedVideoAdClosed");
                loadCustomEventRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                showToastFromAdListener("onRewarded");
                showToastFromAdListener("rewardItem type: " + rewardItem.getType());
                showToastFromAdListener("rewardItem amount: " + rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                showToastFromAdListener("onRewardedVideoAdLeftApplication");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                showToastFromAdListener("onRewardedVideoAdFailedToLoad code: " + errorCode);
                mAdapterVideoButton.setEnabled(true);
            }
        });

        loadCustomEventRewardedVideoAd();
    }

    private void showToastFromAdListener(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdMobRewardedVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdMobRewardedVideoAd.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdMobRewardedVideoAd.destroy(this);
    }

    private void loadCustomEventRewardedVideoAd() {
        mAdapterVideoButton.setEnabled(false);
        mAdMobRewardedVideoAd.loadAd(
                ADMOB_MEDIATION_REWARD_CUSTOM_EVENT_UNIT_ID,
                buildAdRequest()
        );
    }

    private AdRequest buildAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("Your device identifier")
                .build();
    }
}
