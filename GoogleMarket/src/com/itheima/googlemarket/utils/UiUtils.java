package com.itheima.googlemarket.utils;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googlemarket.MyApp;

public class UiUtils {

	/**
	 * 吐司
	 * @param msg
	 */
	public static void showToast(CharSequence msg){
		Toast toast = Toast.makeText(MyApp.getContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static int dp2px(int dp){
		//获取屏幕密度
		float density = MyApp.getContext().getResources().getDisplayMetrics().density;
		//转为px
		int px = (int) (dp*density + 0.5f);
		return px;
	}

	/**创建随机颜色*/
	public static int createRandomColor() {
		Random random = new Random();
		int red =  50 + random.nextInt(150);	// 50 ~ 199的随机颜色值
		int green = 50 + random.nextInt(150);	// 50 ~ 199的随机颜色值
		int blue =  50 + random.nextInt(150);	// 50 ~ 199的随机颜色值
		int color = Color.rgb(red, green, blue);
		return color;
	}
	
	/**创建随机颜色形状选择器的TextView*/
	public static TextView createRandomColorShapeSelectorTextView(Context context) {
		TextView textView  = new TextView(context);
		textView.setPadding(6, 6, 6, 6);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.WHITE);
		
		//设置TextView的随机yanse圆角矩形背景
		textView.setBackgroundDrawable(createRandomColorShapeSelector());
		
		return textView;
	}

	/**设置TextView的随机yanse圆角矩形背景*/
	private static Drawable createRandomColorShapeSelector() {
		//创建状态选择器对象
		StateListDrawable stateListDrawable = new StateListDrawable();
		
		//创建按下状态的形状和背景  和按下状态
		int[] pressState = {android.R.attr.state_pressed, android.R.attr.state_enabled};
		Drawable  pressDrawable = createRandomColorDrawable();
		
		//创建正常状态形状和背景和
		int[] normalState = {};
		Drawable  normalDrawable = createRandomColorDrawable();
		
		stateListDrawable.addState(pressState, pressDrawable);	// 指定按下状态显示的对应的图片
		stateListDrawable.addState(normalState, normalDrawable);// 指定正常状态显示的对应的图片	
		return stateListDrawable;
		
	}

	private static Drawable createRandomColorDrawable() {
		//代码创建shape图形  并设置形状 圆角 颜色
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(dp2px(6));	// 指定圆角的角度
		drawable.setColor(createRandomColor());	// 指定图形的颜色
		return drawable;
	}
}
