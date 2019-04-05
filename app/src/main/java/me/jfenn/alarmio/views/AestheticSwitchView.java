package me.jfenn.alarmio.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import com.afollestad.aesthetic.Aesthetic;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import io.reactivex.disposables.Disposable;
import me.jfenn.alarmio.interfaces.Subscribblable;

public class AestheticSwitchView extends SwitchCompat implements Subscribblable {

    private Disposable colorAccentSubscription;
    private Disposable textColorPrimarySubscription;

    public AestheticSwitchView(Context context) {
        super(context);
    }

    public AestheticSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AestheticSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void subscribe() {
        colorAccentSubscription = Aesthetic.Companion.get().colorAccent()
                .subscribe(integer -> {
                    int[][] states = new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}};

                    ColorStateList colorStateList = new ColorStateList(
                            states,
                            new int[]{
                                    Color.argb(100, 128, 128, 128),
                                    integer
                            }
                    );

                    ColorStateList thumbStateList = new ColorStateList(
                            states,
                            new int[]{
                                    Color.argb(255, 128, 128, 128),
                                    integer
                            }
                    );

                    ColorStateList trackStateList = new ColorStateList(
                            states,
                            new int[]{
                                    Color.argb(100, 128, 128, 128),
                                    Color.argb(100, Color.red(integer), Color.green(integer), Color.blue(integer))
                            }
                    );

                    CompoundButtonCompat.setButtonTintList(AestheticSwitchView.this, colorStateList);

                    if (getThumbDrawable() != null)
                        DrawableCompat.setTintList(DrawableCompat.wrap(getThumbDrawable()), thumbStateList);
                    if (getTrackDrawable() != null)
                        DrawableCompat.setTintList(DrawableCompat.wrap(getTrackDrawable()), trackStateList);
                });

        textColorPrimarySubscription = Aesthetic.Companion.get().textColorPrimary()
                .subscribe(integer -> setTextColor(integer));
    }

    @Override
    public void unsubscribe() {
        colorAccentSubscription.dispose();
        textColorPrimarySubscription.dispose();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        subscribe();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unsubscribe();
    }
}
