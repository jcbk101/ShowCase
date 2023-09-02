package com.genesyseast.showcase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ShowClass
{
    public static final int CIRCLE          = 0;
    public static final int RECTANGLE       = 1;
    public static final int ROUNDED_RECT    = 2;
    public static final int BORDER          = 3;
    public static final int OVAL            = 4;
    //
    public static final int CENTER_OUT      = 0;
    public static final int LEFT_DOWN       = 1;
    public static final int CENTER_OUT_VERT = 2;
    public static final int CENTER_OUT_HORZ = 3;
    private static      int touchToContinue = 1234;
    
    /**
     * //##########################
     * <p>
     * Internal class specifically
     * for ShowCase
     * <p>
     * //##########################
     */
    private static class ShowRectF
            extends RectF
    {
        public ShowRectF()
        {
        }
        
        public ShowRectF( float left, float top, float right, float bottom )
        {
            super( left, top, right, bottom );
        }
        
        public ShowRectF( @Nullable RectF r )
        {
            super( r );
        }
        
        public ShowRectF( @Nullable Rect r )
        {
            super( r );
        }
        
        
        public void setTop( float top )
        {
            this.top = top;
        }
        
        public void setBottom( float bottom )
        {
            this.bottom = bottom;
        }
        
        public void setRight( float right )
        {
            this.right = right;
        }
        
        public void setLeft( float left )
        {
            this.left = left;
        }
    }
    
    
    /**
     * //##########################
     * <p>
     * Internal class specifically
     * for ShowCase
     * <p>
     * //##########################
     */
    private static class showCaseItem
    {
        public  View             target;
        public  ShowRectF        r;
        public  boolean          isDirty;
        public  boolean          isAnimated;
        public  int              startDelay;
        public  TimeInterpolator timeInterpolator;
        public  int              duration;
        private int              showType;
        private int              expandDir;
        private View             tipShow;
        
        public showCaseItem( View target, boolean isAnimated, int duration, int startDelay, TimeInterpolator timeInterpolator, int showType, int expandDir )
        {
            this.target = target;
            this.isAnimated = isAnimated;
            this.isDirty = false;
            this.startDelay = startDelay;
            this.duration = duration;
            this.showType = showType;
            this.timeInterpolator = timeInterpolator;
            this.expandDir = expandDir;
        }
    }
    
    
    /**
     * //#############################
     * <p>
     * Main view being displayed
     * <p>
     * //#############################
     */
    private class ShowCaseView
            extends FrameLayout
            implements View.OnTouchListener
    {
        
        public ShowCaseView( Context context )
        {
            super( context );
            setOnTouchListener( this );
            setBackgroundColor( 0x00000000 );
        }
        
        public ShowCaseView( Context context, @Nullable AttributeSet attrs )
        {
            super( context, attrs );
            setOnTouchListener( this );
            setBackgroundColor( 0x00000000 );
        }
        
        public ShowCaseView( Context context, @Nullable AttributeSet attrs, int defStyleAttr )
        {
            super( context, attrs, defStyleAttr );
            setOnTouchListener( this );
            setBackgroundColor( 0x00000000 );
        }
        
        
        /**
         * //################################
         * <p>
         * Draw what the user wants
         * <p>
         * //################################
         *
         * @param canvas
         */
        @Override
        protected void onDraw( Canvas canvas )
        {
            super.onDraw( canvas );
            
            if ( targets != null && targets.size() > 0 )
            {
                if ( showCaseBmp == null || showCaseCanvas == null )
                {
                    if ( showCaseBmp != null )
                    {
                        showCaseBmp.recycle();
                    }
                    
                    if ( showCaseCanvas != null )
                    {
                        showCaseCanvas = null;
                    }
                    
                    showCaseBmp = Bitmap.createBitmap( getWidth(), getHeight(), Bitmap.Config.ARGB_8888 );
                    showCaseCanvas = new Canvas( showCaseBmp );
                }
                
                
                // Clear the screen back full mask
                showCaseBmp.eraseColor( maskColor );
                
                
                //##################################
                //
                //
                //
                //##################################
                for ( showCaseItem caseItem : targets )
                {
                    if ( caseItem.isDirty || !caseItem.isAnimated )
                    {
                        // Cutout the RoundRect from THIS view--ShowCase parent
                        switch ( caseItem.showType )
                        {
                            case ROUNDED_RECT: showCaseCanvas.drawRoundRect( caseItem.r, 16, 16, paint );
                                break;
                            case RECTANGLE: showCaseCanvas.drawRect( caseItem.r, paint );
                                break;
                            case BORDER:
                                // Adjust the Border so it does not leave dark any parts
                                caseItem.r.left -= padding;
                                caseItem.r.top -= padding;
                                caseItem.r.right += padding;
                                caseItem.r.bottom += padding;
                                //
                                showCaseCanvas.drawRoundRect( caseItem.r, 4, 4, border );
                                break;
                            case OVAL:
                                //
                                // Expand the region for these two ONLY
                                caseItem.r.left -= padding / 2f;
                                caseItem.r.top -= padding / 2f;
                                caseItem.r.right += padding / 2f;
                                caseItem.r.bottom += padding / 2f;
                                
                                // Adjust the OVAL so it does not leave dark any parts
                                showCaseCanvas.drawOval( caseItem.r, paint );
                                break;
                            default:
                                float width = caseItem.r.width();
                                float height = caseItem.r.height();
                                float radius = height;
                                
                                if ( width > height )
                                {
                                    radius = width;
                                }
                                
                                radius /= 2f;
                                showCaseCanvas.drawCircle( caseItem.r.left + radius, caseItem.r.top + radius, radius, paint );
                                break;
                        }
                    }
                }
                
                // Draw ONE TIME, IDIOT!!!
                canvas.drawBitmap( showCaseBmp, 0, 0, null );
            }
        }
        
        
        /**
         * //###########################
         * <p>
         * Touch action
         * <p>
         * //###########################
         *
         * @param v
         * @param event
         *
         * @return
         */
        @Override
        public boolean onTouch( View v, MotionEvent event )
        {
            if ( inSequence )
            {
                if ( event.getActionMasked() == MotionEvent.ACTION_UP && !isAnimatingTarget )
                {
                    // NEED TO SUPPORT TIME SEQUENCES WITH AUTO
                    // NEXT UP!
                    if ( sequence.size() > 0 )
                    {
                        if ( targets.size() > 0 )
                        {
                            View view = targets.get( 0 ).tipShow;
                            vg.removeView( view );
                        }
                        
                        //
                        targets.clear();
                        targets.add( sequence.get( 0 ) );
                        
                        sequence.remove( 0 );
                        inSequence = true;
                        
                        // Hide Touch... when not required
                        if ( sequence.size() < 1 )
                        {
                            View view = showCaseView.findViewById( touchToContinue );
                            if ( view != null )
                            {
                                view.setVisibility( View.INVISIBLE );
                            }
                        }
                        
                        showCaseTargets();
                        return true;
                    }
                    else
                    {
                        inSequence = false;
                    }
                }
                else
                {
                    return true;
                }
            }
            
            //
            //##############################
            //
            if ( isDimmed )
            {
                clearTargets();
                
                setBackgroundColor( Color.TRANSPARENT );
                
                if ( targets != null && targets.size() > 0 )
                {
                    for ( showCaseItem tip : targets )
                    {
                        vg.removeView( tip.tipShow );
                    }
                }
                
                if ( sequence != null && sequence.size() > 0 )
                {
                    for ( showCaseItem tip : sequence )
                    {
                        vg.removeView( tip.tipShow );
                    }
                }
                
                
                showCaseCanvas = null;
                if ( showCaseBmp != null )
                {
                    showCaseBmp.recycle();
                    showCaseBmp = null;
                }
                
                isDimmed = false;
                invalidate();
                
                if ( onShowClassListener != null )
                {
                    onShowClassListener.onComplete();
                }
                
                return true;
            }
            
            return false;
        }
    }
    
    
    /**
     * //#############################
     * <p>
     * All declared variables
     * <p>
     * //#############################
     */
    private ArrayList<showCaseItem> targets;
    private ArrayList<showCaseItem> sequence;
    //
    private Paint                   paint;
    private Paint                   border;
    private Bitmap                  showCaseBmp;
    private Canvas                  showCaseCanvas;
    private ShowCaseView            showCaseView;
    private int                     maskColor         = 0x4F000000;
    private boolean                 isDimmed          = false;
    private boolean                 inSequence        = false;
    private boolean                 isAnimatingTarget = false;
    private OnShowClassListener     onShowClassListener;
    private ViewGroup               vg;
    private int                     padding;
    private Context                 context;
    
    public interface OnShowClassListener
    {
        void onComplete();
    }
    
    
    public Context getContext()
    {
        return this.context;
    }
    
    
    public int getSequenceCount()
    {
        return (sequence != null ? sequence.size() : 0);
    }
    
    
    public int getTargetCount()
    {
        return (targets != null ? targets.size() : 0);
    }
    
    
    /**
     * //##########################
     * <p>
     * Main constructor
     * <p>
     * //##########################
     */
    public ShowClass( Context context )
    {
        this.targets = new ArrayList<>();
        this.sequence = new ArrayList<>();
        this.paint = new Paint();
        this.border = new Paint();
        this.context = context;
        
        // Prepare eraser Paint if needed
        this.paint.setColor( 0xFF000000 );
        this.paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.CLEAR ) );
        this.paint.setFlags( Paint.ANTI_ALIAS_FLAG );
        
        this.border.setColor( 0xFF00FF00 );
        this.border.setStyle( Paint.Style.STROKE );
        this.border.setStrokeWidth( 4 );
        this.paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.CLEAR ) );
        this.border.setFlags( Paint.ANTI_ALIAS_FLAG );
        
        //###########################
        //
        // Where drawings happen
        //
        //###########################
        this.showCaseView = new ShowCaseView( context );
        
        //###########################
        //
        // Create a
        // "Touch to continue"
        // message
        //
        //###########################
        TextView                 cont   = new TextView( getContext() );
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM );
        
        cont.setTextColor( 0xFFFFFFFF );
        cont.setTextSize( 0, getContext().getResources().getDimensionPixelSize( R.dimen._20ssp ) );
        cont.setGravity( Gravity.CENTER_HORIZONTAL );
        cont.setId( 1000 + 234 );
        cont.setText( R.string.touch_to_continue );
        cont.setTypeface( null, Typeface.BOLD );
        cont.setShadowLayer( 2, 0, 0, Color.BLACK );
        
        //
        params.setMargins( 0, 0, 0, getContext().getResources().getDimensionPixelSize( R.dimen._24sdp ) );
        
        //###########################
        //
        // Finalize the Showcase View
        //
        //###########################
        this.showCaseView.addView( cont, params );
        
        this.vg = ( ViewGroup ) (( Activity ) context).getWindow().getDecorView().getRootView();
        this.vg.addView( showCaseView, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        
        //
        this.padding = context.getResources().getDimensionPixelSize( R.dimen._8sdp );
    }
    
    
    /**
     * //##########################
     * <p>
     * Main listener
     * <p>
     * //##########################
     *
     * @param onShowClassListener
     */
    public void setOnShowClassListener( OnShowClassListener onShowClassListener )
    {
        this.onShowClassListener = onShowClassListener;
    }
    
    /**
     * //##########################
     * <p>
     * Check for existing targets
     * <p>
     * //##########################
     *
     * @return
     */
    public boolean hasTargets()
    {
        if ( targets != null )
        {
            return (targets.size() != 0);
        }
        
        return false;
    }
    
    
    /**
     * //##########################
     * <p>
     * Check for existing sequences
     * <p>
     * //##########################
     *
     * @return
     */
    public boolean hasSequences()
    {
        if ( sequence != null )
        {
            return (sequence.size() != 0);
        }
        
        return false;
    }
    
    
    /**
     * //#########################
     * <p>
     * Color used to mask the screen
     * <p>
     * //#########################
     *
     * @param maskColor
     */
    public void setMaskColor( int maskColor )
    {
        this.maskColor = maskColor;
    }
    
    
    /**
     * //#############################
     * <p>
     * Standard view addition
     * <p>
     * //#############################
     *
     * @param target
     *
     * @return
     */
    public ShowClass addTarget( View target, boolean isAnimated, int duration, int startDelay, TimeInterpolator timeInterpolator, int showType, int expandDirection )
    {
        if ( targets != null )
        {
            boolean isPresent = false;
            
            for ( showCaseItem caseItem : targets )
            {
                if ( caseItem.target == target )
                {
                    isPresent = true;
                    break;
                }
            }
            
            if ( !isPresent )
            {
                targets.add( new showCaseItem( target, isAnimated, duration, startDelay, timeInterpolator, showType, expandDirection ) );
            }
        }
        
        return this;
    }
    
    public ShowClass addTarget( View target )
    {
        return addTarget( target, false, 0, 0, null, CIRCLE, CENTER_OUT );
    }
    
    public ShowClass addTarget( View target, int showType )
    {
        return addTarget( target, false, 0, 0, null, showType, CENTER_OUT );
    }
    
    public ShowClass addTarget( Rect rect, boolean isAnimated, int duration, int startDelay, TimeInterpolator timeInterpolator, int showType, int expandDirection )
    {
        if ( rect != null )
        {
            // Get actual screen coordinates
            showCaseItem caseItem = new showCaseItem( null, isAnimated, duration, startDelay, timeInterpolator, showType, expandDirection );
            caseItem.r = new ShowRectF( rect );
            
            targets.add( caseItem );
        }
        
        return this;
    }
    
    
    /**
     * //#################################
     * <p>
     * Add a TIP bubble to this target
     * <p>
     * //#################################
     *
     * @param targetIndex
     * @param tipTitle
     * @param tipMessage
     * @param xPosition
     * @param yPosition
     *
     * @return
     */
    public ShowClass addTargetTip( int targetIndex, String tipTitle, String tipMessage, int xPosition, int yPosition )
    {
        if ( targets != null && targets.size() > 0 && targetIndex > 0 )
        {
            TextView tv;
            View     tip = View.inflate( getContext(), R.layout.tip_layout, null );
            
            tv = tip.findViewById( R.id.tipText );
            tv.setText( tipTitle );
            
            tv = tip.findViewById( R.id.tipSubText );
            tv.setText( tipMessage );
            
            tip.setX( xPosition );
            tip.setY( yPosition - getContext().getResources().getDimensionPixelSize( R.dimen._75sdp ) );
            tip.setLayoutParams( new ViewGroup.LayoutParams( getContext().getResources().getDimensionPixelSize( R.dimen._150sdp ),
                                                             getContext().getResources().getDimensionPixelSize( R.dimen._75sdp )
            ) );
            
            // Assign it
            targets.get( targetIndex - 1 ).tipShow = tip;
        }
        
        return this;
    }
    
    
    /**
     * //#################################
     * <p>
     * Add a TIP bubble to this sequence
     * <p>
     * //#################################
     *
     * @param sequenceIndex
     * @param tipTitle
     * @param tipMessage
     * @param xPosition
     * @param yPosition
     *
     * @return
     */
    public ShowClass addSequenceTip( int sequenceIndex, String tipTitle, String tipMessage, int xPosition, int yPosition )
    {
        if ( sequence != null && sequence.size() > 0 && sequenceIndex > 0 )
        {
            TextView tv;
            View     tip = View.inflate( getContext(), R.layout.tip_layout, null );
            
            
            tv = tip.findViewById( R.id.tipText );
            tv.setText( tipTitle );
            
            tv = tip.findViewById( R.id.tipSubText );
            tv.setText( tipMessage );
            
            tip.setX( xPosition );
            tip.setY( yPosition - getContext().getResources().getDimensionPixelSize( R.dimen._75sdp ) );
            tip.setLayoutParams( new ViewGroup.LayoutParams( getContext().getResources().getDimensionPixelSize( R.dimen._150sdp ),
                                                             getContext().getResources().getDimensionPixelSize( R.dimen._75sdp )
            ) );
            
            sequence.get( sequenceIndex - 1 ).tipShow = tip;
        }
        
        return this;
    }
    
    
    
    /**
     * //############################
     * <p>
     * Standard sequence addition
     * <p>
     * //############################
     */
    public ShowClass addSequence( View target, boolean isAnimated, int duration, int startDelay, TimeInterpolator timeInterpolator, int showType, int expandDirection )
    {
        if ( sequence != null )
        {
            boolean isPresent = false;
            
            for ( showCaseItem caseItem : sequence )
            {
                if ( caseItem.target == target )
                {
                    isPresent = true;
                    break;
                }
            }
            
            if ( !isPresent )
            {
                sequence.add( new showCaseItem( target, isAnimated, duration, startDelay, timeInterpolator, showType, expandDirection ) );
            }
        }
        
        return this;
    }
    
    public ShowClass addSequence( View target )
    {
        return addSequence( target, false, 0, 0, null, CIRCLE, CENTER_OUT );
    }
    
    public ShowClass addSequence( View target, int showType )
    {
        return addSequence( target, false, 0, 0, null, showType, CENTER_OUT );
    }
    
    public ShowClass addSequence( Rect rect, boolean isAnimated, int duration, int startDelay, TimeInterpolator timeInterpolator, int showType, int expandDirection )
    {
        if ( rect != null )
        {
            // Get actual screen coordinates
            showCaseItem caseItem = new showCaseItem( null, isAnimated, duration, startDelay, timeInterpolator, showType, expandDirection );
            caseItem.r = new ShowRectF( rect );
            
            sequence.add( caseItem );
        }
        
        return this;
    }
    
    
    /**
     * //############################
     * <p>
     * Remove from the list
     * <p>
     * //############################
     *
     * @param target
     *
     * @return
     */
    public ShowClass removeTarget( View target )
    {
        if ( targets != null )
        {
            for ( showCaseItem caseItem : targets )
            {
                if ( caseItem.target == target )
                {
                    caseItem.target = null;
                    caseItem.r = null;
                    //
                    targets.remove( caseItem );
                }
            }
        }
        
        return this;
    }
    
    
    /**
     * //############################
     * <p>
     * Remove from the list
     * <p>
     * //############################
     */
    public void endShowCase()
    {
        try
        {
            if ( vg != null )
            {
                // Remove any added text views, etc
                for ( int i = 0; i < vg.getChildCount(); i++ )
                {
                    View v = vg.getChildAt( i );
                    
                    if ( v.getTag() != null )
                    {
                        (( ObjectAnimator ) v.getTag()).end();
                        //
                        vg.removeView( v );
                        v = null;
                    }
                }
                
                //
                View cont = showCaseView.findViewById( touchToContinue );
                if ( cont != null && cont.getTag() != null )
                {
                    (( ObjectAnimator ) cont.getTag()).end();
                    cont.setTag( null );
                }
                
                vg.removeView( showCaseView );
                onDestroy();
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * //############################
     * <p>
     * Remove all targets
     * <p>
     * //############################
     */
    public void clearTargets()
    {
        if ( targets != null )
        {
            targets.clear();
        }
    }
    
    
    /**
     * //############################
     * <p>
     * Start the show
     * <p>
     * //############################
     */
    public void showCaseTargets()
    {
        //########################################
        //
        //
        //
        //########################################
        for ( final showCaseItem caseItem : targets )
        {
            ObjectAnimator show;
            int[]          xyPosi = new int[ 2 ];
    
            isDimmed = true;
    
    
            //########################################
            //
            // Use Rect
            //
            //########################################
            if ( caseItem.target == null && caseItem.r != null )
            {
                final float w = (caseItem.r.right - caseItem.r.left);
                final float h = (caseItem.r.bottom - caseItem.r.top);
                
                if ( caseItem.isAnimated )
                {
                    show = getExpandDirection( caseItem, w, h );
                    //
                    
                    show.setDuration( caseItem.duration ).setInterpolator( caseItem.timeInterpolator );
                    show.setStartDelay( caseItem.startDelay );
                    show.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
                    {
                        @Override
                        public void onAnimationUpdate( ValueAnimator animation )
                        {
                            isAnimatingTarget = true;
                            caseItem.isDirty = true;
                            showCaseView.postInvalidate();
                        }
                    } );
                    show.addListener( new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd( Animator animation )
                        {
                            super.onAnimationEnd( animation );
                            
                            isAnimatingTarget = false;
                            
                            if ( caseItem.tipShow != null )
                            {
                                vg.addView( caseItem.tipShow );
                                animateTip( caseItem.tipShow );
                            }
                        }
                    } );
                    show.start();
                }
                else
                {
                    // Get actual screen coordinates
                    caseItem.isDirty = true;
                    showCaseView.postInvalidate();
                    
                    if ( caseItem.tipShow != null )
                    {
                        vg.addView( caseItem.tipShow );
                        animateTip( caseItem.tipShow );
                    }
                }
            }
            //#############################
            //
            // Standard Views
            //
            //#############################
            else if ( caseItem.target != null && caseItem.target.getVisibility() == View.VISIBLE )
            {
                final float w = caseItem.target.getWidth();
                final float h = caseItem.target.getHeight();
                
                if ( w > 0 && h > 0 )
                {
                    if ( caseItem.isAnimated )
                    {
                        if ( caseItem.r == null )
                        {
                            caseItem.r = new ShowRectF();
                        }
                        
                        // Get actual screen coordinates
                        caseItem.target.getLocationOnScreen( xyPosi );
                        
                        //                        xyPosi[ 1 ] -= adjustHeight;
                        
                        caseItem.r.left = xyPosi[ 0 ];
                        caseItem.r.right = xyPosi[ 0 ] + w;
                        caseItem.r.top = xyPosi[ 1 ];
                        caseItem.r.bottom = xyPosi[ 1 ] + h;
                        
                        
                        show = getExpandDirection( caseItem, w, h );
                        //
                        show.setDuration( caseItem.duration ).setInterpolator( caseItem.timeInterpolator );
                        show.setStartDelay( caseItem.startDelay );
                        show.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate( ValueAnimator animation )
                            {
                                isAnimatingTarget = true;
                                caseItem.isDirty = true;
                                showCaseView.postInvalidate();
                            }
                        } );
                        show.addListener( new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd( Animator animation )
                            {
                                super.onAnimationEnd( animation );
                                
                                isAnimatingTarget = false;
                                
                                if ( caseItem.tipShow != null )
                                {
                                    vg.addView( caseItem.tipShow );
                                    animateTip( caseItem.tipShow );
                                }
                            }
                        } );
                        show.start();
                    }
                    else
                    {
                        if ( caseItem.r == null )
                        {
                            // Get actual screen coordinates
                            caseItem.target.getLocationOnScreen( xyPosi );
                            //                            xyPosi[ 1 ] -= adjustHeight;
                            //                        caseItem.r = new ShowRectF( caseItem.target.getLeft(), caseItem.target.getTop(), caseItem.target.getRight(), caseItem.target.getBottom() );
                            caseItem.r = new ShowRectF( xyPosi[ 0 ], xyPosi[ 1 ], xyPosi[ 0 ] + w, xyPosi[ 1 ] + h );
                        }
                        
                        caseItem.isDirty = true;
                        showCaseView.postInvalidate();
                        if ( caseItem.tipShow != null )
                        {
                            vg.addView( caseItem.tipShow );
                            animateTip( caseItem.tipShow );
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * //############################
     * <p>
     * Animate the tip
     * <p>
     * //############################
     *
     * @param tipShow
     */
    private void animateTip( final View tipShow )
    {
        tipShow.setScaleY( .85f );
        tipShow.setScaleX( .85f );
        tipShow.animate().setDuration( 750 ).setInterpolator( new BounceInterpolator() );
        tipShow.animate().scaleX( 1 ).scaleY( 1 ).withEndAction( new Runnable()
        {
            @Override
            public void run()
            {
                PropertyValuesHolder sx = PropertyValuesHolder.ofFloat( "scaleX", .97f, 1f, .97f );
                PropertyValuesHolder sy = PropertyValuesHolder.ofFloat( "scaleY", .97f, 1f, .97f );
                
                ObjectAnimator obj = ObjectAnimator.ofPropertyValuesHolder( tipShow, sx, sy );
                obj.setDuration( 2000 ).setRepeatCount( ValueAnimator.INFINITE );
                obj.start();
                tipShow.setTag( obj );
            }
        } ).start();
    }
    
    
    /**
     * //############################
     * <p>
     * Start the sequence
     * <p>
     * //############################
     */
    public void startSequence( int sequenceDelay )
    {
        if ( sequence.size() > 0 )
        {
            targets.clear();
            targets.add( sequence.get( 0 ) );
            
            sequence.remove( 0 );
            inSequence = true;
            
            //
            View v = showCaseView.findViewById( touchToContinue );
            if ( v != null )
            {
                v.setVisibility( View.VISIBLE );
                animateTip( v );
            }
            
            showCaseTargets();
        }
        else
        {
            inSequence = false;
        }
    }
    
    
    /**
     * //############################
     * <p>
     * Determine what expand
     * animation is used
     * <p>
     * //############################
     *
     * @param caseItem
     *
     * @return
     */
    private ObjectAnimator getExpandDirection( showCaseItem caseItem, float w, float h )
    {
        
        PropertyValuesHolder rl, rt;
        PropertyValuesHolder rr, rb;
        
        if ( caseItem.expandDir == CENTER_OUT_HORZ )
        {
            rl = PropertyValuesHolder.ofFloat( "left", caseItem.r.left + (w / 2f), caseItem.r.left );
            rr = PropertyValuesHolder.ofFloat( "right", caseItem.r.right - (w / 2f), caseItem.r.right );
            rt = PropertyValuesHolder.ofFloat( "top", caseItem.r.top, caseItem.r.top );
            rb = PropertyValuesHolder.ofFloat( "bottom", caseItem.r.bottom, caseItem.r.bottom );
        }
        else if ( caseItem.expandDir == LEFT_DOWN )
        {
            rl = PropertyValuesHolder.ofFloat( "left", caseItem.r.left, caseItem.r.left );
            rr = PropertyValuesHolder.ofFloat( "right", caseItem.r.left, caseItem.r.right );
            rt = PropertyValuesHolder.ofFloat( "top", caseItem.r.top, caseItem.r.top );
            rb = PropertyValuesHolder.ofFloat( "bottom", caseItem.r.top, caseItem.r.bottom );
        }
        else if ( caseItem.expandDir == CENTER_OUT_VERT )
        {
            rl = PropertyValuesHolder.ofFloat( "left", caseItem.r.left, caseItem.r.left );
            rr = PropertyValuesHolder.ofFloat( "right", caseItem.r.right, caseItem.r.right );
            rt = PropertyValuesHolder.ofFloat( "top", caseItem.r.top + (h / 2f), caseItem.r.top );
            rb = PropertyValuesHolder.ofFloat( "bottom", caseItem.r.bottom - (h / 2f), caseItem.r.bottom );
        }
        else
        {  // CENTER_OUT
            rl = PropertyValuesHolder.ofFloat( "left", caseItem.r.left + (w / 2f), caseItem.r.left );
            rr = PropertyValuesHolder.ofFloat( "right", caseItem.r.right - (w / 2f), caseItem.r.right );
            rt = PropertyValuesHolder.ofFloat( "top", caseItem.r.top + (h / 2f), caseItem.r.top );
            rb = PropertyValuesHolder.ofFloat( "bottom", caseItem.r.bottom - (h / 2f), caseItem.r.bottom );
        }
        
        return ObjectAnimator.ofPropertyValuesHolder( caseItem.r, rl, rt, rr, rb );
    }
    
    
    /**
     * //############################
     * <p>
     * Must free the memory
     * <p>
     * //############################
     */
    protected void onDestroy()
    {
        targets = null;
        sequence = null;
        
        paint = null;
        border = null;
        if ( showCaseBmp != null )
        {
            showCaseBmp.recycle();
            showCaseBmp = null;
        }
        
        if ( showCaseCanvas != null )
        {
            showCaseCanvas = null;
        }
        
        showCaseView.removeAllViews();
        
        showCaseView = null;
        onShowClassListener = null;
        
        vg = null;
        context = null;
    }
}
