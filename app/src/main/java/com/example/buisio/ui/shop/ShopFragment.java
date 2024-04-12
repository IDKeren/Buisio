package com.example.buisio.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.buisio.databinding.FragmentShopBinding;

public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShopViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ShopViewModel.class);

        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textShop;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}