package com.example.gatemtquiz3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class SolutionsActivity2 extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private String AdTAG = SolutionsActivity2.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    private static final String TAG = SolutionsActivity2.class.getSimpleName();
    public static final String SAMPLE_FILE = MainActivity.solutionPdfName;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions2);

        mInterstitialAd = new InterstitialAd(this);

        if (savedInstanceState == null) {
            showInterScreenAd();
        }
        pdfView= (PDFView)findViewById(R.id.pdfView);
        displayFromAsset(SAMPLE_FILE);

    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    private void showInterScreenAd() {
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        //Test add Id mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        // setting listener for Ad
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
