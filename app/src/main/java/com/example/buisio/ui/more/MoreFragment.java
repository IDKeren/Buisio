package com.example.buisio.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.buisio.Login;
import com.example.buisio.R;
import com.example.buisio.databinding.FragmentMoreBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MoreFragment extends Fragment {
    private ImageButton logoutBTN;
    private FragmentMoreBinding binding;

    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MoreViewModel moreViewModel =
                new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        logoutBTN = root.findViewById(R.id.logout);
        logout();
        return root;
    }



    private void logout() {
        logoutBTN.setOnClickListener(view -> {
            auth.getInstance().signOut();
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}