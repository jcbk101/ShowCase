package com.genesyseast.showcase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.security.SecureRandom;

public class MainActivity
        extends AppCompatActivity
{
    private ShowClass showClass;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );
        
        //        final ShowClass showClass = findViewById( R.id.showCase );
        TextView button = findViewById( R.id.showButton );
        
        final StarsEarnedBar bar = findViewById( R.id.starsToEarn );
        bar.setMax( 1000 );
        bar.setProgress( 500 );
        bar.setStarsPosi( "200,400,900" );
        
        
        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                // Takes care of adding the ShowCaseView to
                // The Main Window Decor
                SecureRandom r = new SecureRandom(  );
                
                bar.setProgress( r.nextInt( bar.getMax()) );
                bar.requestLayout();
/*
                showClass = new ShowClass( MainActivity.this );
                
                showClass.addSequence( findViewById( R.id.avatar1 ), true, 1500, 0, new BounceInterpolator(), ShowClass.RECTANGLE, ShowClass.LEFT_DOWN );
                showClass.addSequence( findViewById( R.id.image1 ), ShowClass.CIRCLE );
                showClass.addSequence( findViewById( R.id.image2 ), true, 2000, 250, new OvershootInterpolator(), ShowClass.ROUNDED_RECT, ShowClass.CENTER_OUT_VERT );
                showClass.addSequence( findViewById( R.id.showButton ), true, 1000, 250, new LinearInterpolator(), ShowClass.OVAL, ShowClass.CENTER_OUT );
                
                int[] xy   = new int[ 2 ];
                Rect  r;
                View  view = findViewById( R.id.helloWorld );
                
                view.getLocationOnScreen( xy );
                
                r = new Rect( xy[ 0 ], xy[ 1 ], xy[ 0 ] + view.getWidth(), xy[ 1 ] + view.getHeight() );
                r.left -= 8;
                r.right += 8;
                r.top -= 8;
                r.bottom += 8;
                
                //                showClass.addTarget( new Rect( xy[ 0 ] - 100, xy[ 1 ] - 100, xy[ 0 ] + (view.getWidth()/2), xy[ 1 ] + (view.getHeight()/2)  ), true, 1000, 0, new OvershootInterpolator(), ShowClass.ROUNDED_RECT );
                showClass.addSequence( r, true, 1000, 0, new OvershootInterpolator(), ShowClass.ROUNDED_RECT, ShowClass.CENTER_OUT_HORZ )
                         .addSequenceTip( showClass.getSequenceCount(), "Hello Label", "This is a three-step blocker. You should be aware.", r.left, r.top );
                
                showClass.setMaskColor( 0xAF000000 );
                showClass.startSequence( 0 );
                showClass.setOnShowClassListener( new ShowClass.OnShowClassListener()
                {
                    @Override
                    public void onComplete()
                    {
                        // This handles removing the ShowCaseView
                        // from the Window decor
                        showClass.endShowCase();
                        showClass = null;
                    }
                } );
*/
            }
        } );
    }
}