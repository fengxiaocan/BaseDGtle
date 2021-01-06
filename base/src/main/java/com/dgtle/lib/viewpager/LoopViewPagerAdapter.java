package com.dgtle.lib.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限循环的ViewPager
 *
 * @param <T>
 * @param <V>
 */
public abstract class LoopViewPagerAdapter<T,V extends ViewPagerViewHolder> extends PagerAdapter {
    protected List<T> mData;
    protected boolean isLoop;
    protected int loopAllCount = Integer.MAX_VALUE;
    protected ViewPagerViewHolder mCurrentPrimaryItem = null;

    public LoopViewPagerAdapter(){
    }

    public List<T> getData(){
        return mData;
    }

    public void setData(T... data){
        if(data != null){
            if(mData == null){
                mData = new ArrayList<>();
            }
            mData.clear();
            for(T da: data){
                mData.add(da);
            }
        }
    }

    public void setData(List<T> data){
        mData = data;
    }

    @Override
    public void notifyDataSetChanged(){
        isLoop = mData != null && mData.size() > 1;
        if(mData != null && mData.size() > 0){
            loopAllCount = Integer.MAX_VALUE / mData.size() * mData.size();
        } else{
            loopAllCount = Integer.MAX_VALUE;
        }
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        return super.getItemPosition(object);
    }

    public void addData(T... data){
        if(data != null){
            if(mData == null){
                mData = new ArrayList<>();
            }
            for(T da: data){
                mData.add(da);
            }
        }
    }

    public void setDataAndNotify(List<T> data){
        setData(data);
        notifyDataSetChanged();
    }

    public void addData(List<T> data){
        if(mData == null){
            mData = new ArrayList<>();
        }
        mData.addAll(data);
    }

    public void addDataAndNotify(List<T> data){
        addData(data);
        notifyDataSetChanged();
    }

    public void addData(T t){
        if(mData == null){
            mData = new ArrayList<>();
        }
        mData.add(t);
    }

    public void addDataAndNotify(T t){
        addData(t);
        notifyDataSetChanged();
    }

    public void insertData(T t,int position){
        if(mData == null){
            mData = new ArrayList<>();
            mData.add(t);
        } else{
            if(position < 0){
                mData.add(0,t);
            } else if(mData.size() > position){
                mData.add(t);
            } else{
                mData.add(position,t);
            }
        }
    }

    public void insertDataAndNotify(T t,int position){
        insertData(t,position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        if(mData != null){
            if(isLoop){
                return loopAllCount;
            }
            return mData.size();
        }
        return 0;
    }

    public int getRealCount(){
        if(mData != null){
            return mData.size();
        }
        return 0;
    }

    public T getItemData(int position){
        return mData.get(position % mData.size());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        T data = getItemData(position);
        V holder = createViewHolder(container,data,position);
        container.addView(holder.getRootView());

        holder.findViewById(holder.mRootView);
        holder.initData(data,position);

        return holder;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        ((ViewPagerViewHolder)object).destroyItem(container,position);
    }


    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        ViewPagerViewHolder viewHolder = (ViewPagerViewHolder)object;
        if(viewHolder != mCurrentPrimaryItem){
            if(mCurrentPrimaryItem != null){
                mCurrentPrimaryItem.onPagerVisibleHint(false);
            }
            if(viewHolder != null){
                viewHolder.onPagerVisibleHint(true);
            }
            mCurrentPrimaryItem = viewHolder;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
        return view == ((ViewPagerViewHolder)object).getRootView();
    }

    public abstract V createViewHolder(ViewGroup container, T data, int position);

}
