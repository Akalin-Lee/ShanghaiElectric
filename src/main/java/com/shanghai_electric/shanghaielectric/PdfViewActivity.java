package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        PDFView pdfView = (PDFView)findViewById(R.id.pdfView);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        pdfView.fromUri(uri)
//                .pages(0, 2, 1, 3, 3, 3)
                // all pages are displayed by default
//                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
//                .onDraw(new OnDrawListener() {
//                    @Override
//                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//
//                    }
//                })
//                .onLoad(new OnLoadCompleteListener() {
//                    @Override
//                    public void loadComplete(int nbPages) {
//
//                    }
//                })
//                .onPageChange(new OnPageChangeListener() {
//                    @Override
//                    public void onPageChanged(int page, int pageCount) {
//
//                    }
//                })
//                .onPageScroll(new OnPageScrollListener() {
//                    @Override
//                    public void onPageScrolled(int page, float positionOffset) {
//
//                    }
//                })
//                .onError(new OnErrorListener() {
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//                })
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load();
    }
}
