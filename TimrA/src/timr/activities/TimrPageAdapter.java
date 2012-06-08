package timr.activities;

import timr.android.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class TimrPageAdapter extends PagerAdapter {

        public int getCount() {
            return 7;
        }

        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int resId = 0;
            switch (position) {
            case 1:
                resId = R.layout.time_list_monday;
                break;
            case 2:
                resId = R.layout.time_list_tuesday;
                break;
            case 3:
                resId = R.layout.time_list_wednesday;
                break;
            case 4:
                resId = R.layout.time_list_thursday;
                break;
            case 5:
                resId = R.layout.time_list_friday;
                break;
            case 6:
                resId = R.layout.time_list_saturday;
                break;
            case 0:
                resId = R.layout.time_list_sunday;
                break;
            }

            View view = inflater.inflate(resId, null);

            ((ViewPager) collection).addView(view, 0);
            
           // view.setBackgroundColor(Color.BLUE);
            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public Parcelable saveState() {
            return null;
        }
}