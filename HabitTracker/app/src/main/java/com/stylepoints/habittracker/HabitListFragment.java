package com.stylepoints.habittracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;

import java.util.List;


public class HabitListFragment extends Fragment {
    private TextView tvHabitList;
    private ListView lvHabitList;

    public HabitListFragment() {
        // Required empty constructor
    }

    /**
     * Factory method used to create a new instance of this fragment
     * Usually we pass some data in here, but don't need to for this fragment
     * @return A new instance of fragment HabitListFragment
     */
    public static HabitListFragment newInstance() {
        return new HabitListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HabitRepository repo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getContext()));
        HabitListViewModelFactory factory = new HabitListViewModelFactory(repo);
        HabitListViewModel model = ViewModelProviders.of(this, factory).get(HabitListViewModel.class);

        subscribeToModel(model);
    }

    private void subscribeToModel(final HabitListViewModel model) {
        // observe the list of habits from the database. When a list item is changed
        // this method is called so we can update the UI
        model.getHabitList().observe(this, new Observer<List<HabitEntity>>() {
            @Override
            public void onChanged(@Nullable List<HabitEntity> habitEntities) {
                // update our UI
                // TODO: Change to RecyclerView, or ListView?
                if (habitEntities == null) {
                    return;
                }
                tvHabitList.setText("Number of habits: " + String.valueOf(habitEntities.size()) + "\n\n");
                for (HabitEntity habit : habitEntities) {
                    tvHabitList.append(String.valueOf(habit.getId()) + ": ");
                    tvHabitList.append(habit.getType() + "   " + habit.getReason() + "\n\n");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Bind to the UI
        tvHabitList = (TextView) view.findViewById(R.id.tv_habit_list);
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
