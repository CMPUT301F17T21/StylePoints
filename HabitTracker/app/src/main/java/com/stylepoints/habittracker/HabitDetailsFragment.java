package com.stylepoints.habittracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment; // just using android.app.Fragment doesn't work??
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitViewModel;
import com.stylepoints.habittracker.viewmodel.HabitViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HabitDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HabitDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_HABIT_ID = "habit_id";
    private int habitId;

    private TextView tvHabitType;
    private TextView tvHabitReason;
    private TextView tvHabitSchedule;
    private TextView tvStartDate;

    private FloatingActionButton fabEditHabit;

    private OnFragmentInteractionListener mListener;

    public HabitDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitId the ID of the habit we want to get from the database
     * @return A new instance of fragment HabitDetailsFragment.
     */
    public static HabitDetailsFragment newInstance(int habitId) {
        HabitDetailsFragment fragment = new HabitDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_HABIT_ID, habitId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            habitId = getArguments().getInt(KEY_HABIT_ID);
        }

        // Initialize our view model. The ViewModel contains the model (data) required for that
        // specific view.
        HabitRepository repo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getContext()));
        HabitViewModelFactory factory = new HabitViewModelFactory(repo, habitId);
        HabitViewModel model = ViewModelProviders.of(this, factory).get(HabitViewModel.class);

        subscribeToModel(model);
    }

    private void subscribeToModel(final HabitViewModel model) {
        // observe our Habit from the database. When it gets changed in the database
        // this method is called to update the UI
        model.getHabit().observe(this, new Observer<HabitEntity>() {
            @Override
            public void onChanged(@Nullable HabitEntity habitEntity) {
                // update our UI
                if (habitEntity != null) {
                    tvHabitType.setText(habitEntity.getType());
                    tvHabitReason.setText(habitEntity.getReason());
                    tvStartDate.setText(habitEntity.getStartDate().toString());
                    tvHabitSchedule.setText(habitEntity.getSchedule());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Bind to the UI

        tvHabitType = (TextView) view.findViewById(R.id.tv_habit_type);
        tvHabitReason = (TextView) view.findViewById(R.id.tv_habit_reason);
        tvStartDate = (TextView) view.findViewById(R.id.tv_habit_start_date);
        tvHabitSchedule = (TextView) view.findViewById(R.id.tv_habit_schedule);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
