package wb.com.yoyo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import wb.com.yoyo.R;


public class FindFragment extends Fragment {
	
	ViewGroup findView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_find, null);
		findView=(ViewGroup) view.findViewById(R.id.re_fujin);
		
		init();
		return view;
	}

	
	private void init() {
		// TODO Auto-generated method stub
		findView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Intent intent=new Intent(getActivity(),NearlyActivity.class);
			//	startActivity(intent);
			}
		});
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
		
	
}
