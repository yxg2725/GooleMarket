package com.itheima.googlemarket.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RatioImageView extends ImageView {

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//获取显示图片的自定义控件
		Drawable drawable = getDrawable();
		if (drawable != null) {
			//获取真实的高和宽
			int minimumHeight = drawable.getMinimumHeight();
			int minimumWidth = drawable.getMinimumWidth();
			
			//计算款高比
			float scale = (float)minimumHeight/minimumWidth;
			
			//计算父容器的测量的宽
			int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
			//按比例算出应该显示的高
			int displayHeight = (int) (measureWidth * scale);
			
			//因为宽是匹配父窗体 属于精确模式 所以要把宽设置为精确模式
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(displayHeight, MeasureSpec.EXACTLY);
			
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	

}
