package com.snh.snhseller.ui.salesmanManagement.cardRecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snh.snhseller.R;
import com.test.tudou.library.model.CalendarDay;
import com.test.tudou.library.util.DayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class SimpleFragment extends Fragment {

    @BindView(R.id.text)
    TextView text;
    Unbinder unbinder;

    private CalendarDay mCalendarDay;

    public static SimpleFragment newInstance(CalendarDay calendarDay) {
        SimpleFragment simpleFragment = new SimpleFragment();
        simpleFragment.mCalendarDay = calendarDay;
        return simpleFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text.setText("This is at: " + DayUtils.formatEnglishTime(mCalendarDay.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
