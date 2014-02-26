package com.gren.decoration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 显示项
 */

public class PersonelSettingFragment extends Fragment {

	//private int mPos = -1;
	//private int mImgRes;
	
	public PersonelSettingFragment() { }
//	public PersonelSettingFragment(int pos) {
//		mPos = pos;
//	}
	
	 /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static PersonelSettingFragment newInstance(String imageUrl) {
        final PersonelSettingFragment f = new PersonelSettingFragment();

//        final Bundle args = new Bundle();
//        args.putString(IMAGE_DATA_EXTRA, imageUrl);
//        f.setArguments(args);

        return f;
    }

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mTotalView = inflater.inflate(R.layout.account_frame, null);
//		if (mPos == -1 && savedInstanceState != null)
//			mPos = savedInstanceState.getInt("mPos");
//		TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
//		mImgRes = imgs.getResourceId(mPos, -1);
//		
//		GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
//		gv.setBackgroundResource(android.R.color.black);
//		gv.setAdapter(new GridAdapter());
//		gv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				if (getActivity() == null)
//					return;
//				MainActivity activity = (MainActivity) getActivity();
//				activity.onItemPressed(mPos);
//			}			
//		});
//		return gv;
		return mTotalView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("mPos", mPos);
	}
}
